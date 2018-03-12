package com.noash.poke.component;

import com.alibaba.fastjson.JSON;
import com.mongodb.BasicDBObject;
import com.noash.poke.constant.Regexp;
import com.noash.poke.pojo.dto.KeyAndList;
import com.noash.poke.pojo.dto.Progress;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Component
public class WebFetcher {

    @Autowired
    private MongoTemplate mongo;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${pokemon-data.url.constants}")
    private String constantsUrl;
    @Value("${pokemon-data.url.pokemon-list}")
    private String pokemonListUrl;
    @Value("${pokemon-data.url.ability-list}")
    private String abilityListUrl;
    @Value("${pokemon-data.url.move-list}")
    private String moveListUrl;
    @Value("${pokemon-data.url.item-list}")
    private String itemListUrl;
    @Value("${pokemon-data.url.ability-detail-prefix}")
    private String abilityDetailUrlPrefix;
    @Value("${pokemon-data.url.item-detail-prefix}")
    private String itemDetailUrlPrefix;
    @Value("${pokemon-data.url.move-detail-prefix}")
    private String moveDetailUrlPrefix;
    @Value("${pokemon-data.url.pokemon-detail-prefix}")
    private String pokemonDetailUrlPrefix;
    @Value("${pokemon-data.version}")
    private String version;
    @Value("${pokemon-data.dex-total}")
    private Integer dexTotal;

    void fetchConstants() {
        log.info("----------FETCH constants----------");
        log.info("Connecting to {} to get data ...", constantsUrl);
        String rawData = restTemplate.getForObject(constantsUrl, String.class);
        log.info("Data got successfully!");

        String value = rawData.split(" = ")[1];// 取出赋值的对象
        String jsonStr = value.substring(0, value.length() - 1);// 去除尾部分号
        jsonStr = correctJsonKeys(jsonStr);

        String collection = "constants_" + version;
        log.info("Inserting data into collection: {} ...", collection);
        mongo.insert(JSON.parse(jsonStr), collection);
        log.info("Data inserted successfully!");
    }

    void fetchAbilityList() {
        fetchList("ability-list", abilityListUrl, 2);
    }

    void fetchItemList() {
        log.info("----------FETCH item-list----------");
        log.info("Connecting to {} to get rawData ...", itemListUrl);
        List<List<Object>> rawData = restTemplate.getForObject(itemListUrl, List.class);
        log.info("Data got successfully!");

        List<KeyAndList> dataList = rawData.stream()
            .map(list -> mapToDocument(list, 2))
            .filter(k -> !k.getKey().equals("contest-costume") && !k.getKey().equals("???")) // 移除脏数据
            .collect(Collectors.toList());

        String collection = "item-list_" + version;
        log.info("Inserting data into collection: {} ...", collection);
        mongo.insert(dataList, collection);
        mongo.indexOps(collection).ensureIndex(new Index("key", Sort.Direction.ASC).named("key_1").unique());
        log.info("Data inserted successfully!");
    }

    void fetchMoveList() {
        fetchList("move-list", moveListUrl, 0);
    }

    void fetchPokemonList() {
        fetchList("pokemon-list", pokemonListUrl, 0);
    }

    void fetchAbilityDetails() throws IOException {
        fetchDetailsFromHtml("ability", abilityDetailUrlPrefix);
    }

    void fetchItemDetails() throws IOException {
        fetchDetailsFromHtml("item", itemDetailUrlPrefix);
    }

    void fetchMoveDetails() throws IOException {
        fetchDetailsFromHtml("move", moveDetailUrlPrefix);
    }

    void renameKeyForMoveDetails() {
        mongo.updateMulti(new BasicQuery("{}"), new Update().rename("class", "clazz"), "move_" + version);
    }

    void saveProgressForPokemonDetails() {
        Progress progress = new Progress();
        progress.setVersion(version);
        progress.setNumber(0);
        for (int i = 0; i < 4; i++) {
            progress.setStep(i);
            mongo.insert(progress);
        }
    }

    void fetchPokemonDetails() {
        log.info("----------FETCH pokemon details----------");
        Query query = new BasicQuery(new BasicDBObject("version", version).append("step", 0));
        int fetchedNum = mongo.findOne(query, Progress.class).getNumber();
        log.info("Already fetched {} records", fetchedNum);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Requested-With", "XMLHttpRequest");
        String collection = "pokemon_" + version;

        while (fetchedNum < dexTotal) {
            String url = pokemonDetailUrlPrefix + ++fetchedNum + "/ajax";
            log.info("Connecting to {} to get data ...", url);
            String jsonStr = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class).getBody();
            jsonStr = correctJsonKeys(jsonStr);

            log.info("Insert data into collection: {}", collection);
            Object data = JSON.parseObject(jsonStr).get("pokemon");
            mongo.insert(data, collection);

            log.info("Update progress for fetched record");
            mongo.updateFirst(query, Update.update("number", fetchedNum), Progress.class);

            log.info("No.{} record fetched successfully!", fetchedNum);
        }
    }

    void createIndexForPokemonDetails() {
        mongo.indexOps("pokemon_" + version).ensureIndex(new Index("nat_id", Sort.Direction.ASC).named("nat_id_1").unique());
    }

    private String correctJsonKeys(String jsonStr) {
        // 用'-'替换json键中的'.'
        Pattern pattern = Pattern.compile(Regexp.KEY_CONTAINS_DOT);
        Matcher matcher = pattern.matcher(jsonStr);

        while (matcher.find()) {
            String oldStr = matcher.group();
            String newStr = oldStr.replace(".", "-");

            jsonStr = jsonStr.replace(oldStr, newStr);
        }

        return jsonStr;
    }

    private void fetchDetailsFromHtml(String module, String urlPrefix) throws IOException {
        log.info("----------FETCH {} details----------", module);
        // 获取需要爬取的URL后缀集合
        log.info("Get not fetched Records");
        String listCollection = module + "-list_" + version;
        Query query = new BasicQuery("{'fetched':{'$ne':true}}", "{'key':1}");
        List<KeyAndList> dataList = mongo.find(query, KeyAndList.class, listCollection);

        int count = 0;
        for (KeyAndList data : dataList) {
            String url = urlPrefix + data.getKey();
            log.info("Connect to {} to get rawData ...", url);
            Document document = Jsoup.connect(url).get();
            String rawData = document.body().select("main").select("script").html();

            // 取出JS中的JSON数据
            log.info("Parse rawData to jsonData");
            Matcher matcher = Pattern.compile(Regexp.JSON_IN_SCRIPT).matcher(rawData);
            if (matcher.find()) {
                String jsonStr = matcher.group(1);
                String collection = module + "_" + version;
                log.info("Insert data into collection: {}", collection);
                mongo.insert(JSON.parse(jsonStr), collection);

                log.info("Update status for fetched record");
                mongo.updateFirst(new BasicQuery(new BasicDBObject("key", data.getKey())), Update.update("fetched", true), listCollection);
                count++;
            }
        }

        log.info("Total Records: {}", dataList.size());
        log.info("Fetched Records: {}", count);
    }

    private void fetchList(String module, String url, int keyIndex) {
        log.info("----------FETCH {}----------", module);
        log.info("Connecting to {} to get rawData ...", url);
        List<List<Object>> rawData = restTemplate.getForObject(url, List.class);
        log.info("Data got successfully!");

        List<KeyAndList> dataList = rawData.stream()
            .map(list -> mapToDocument(list, keyIndex))
            .collect(Collectors.toList());

        String collection = module + "_" + version;
        log.info("Inserting data into collection: {} ...", collection);
        mongo.insert(dataList, collection);
        mongo.indexOps(collection).ensureIndex(new Index("key", Sort.Direction.ASC).named("key_1").unique());
        log.info("Data inserted successfully!");
    }

    private KeyAndList mapToDocument(List<Object> list, int keyIndex) {
        KeyAndList keyAndList = new KeyAndList();
        Object key = list.get(keyIndex);
        if (key instanceof String) {
            keyAndList.setKey(((String) key).toLowerCase().replace(" ", "-"));
        } else {
            keyAndList.setKey(key.toString());
        }
        keyAndList.setList(list);

        return keyAndList;
    }
}
