package com.noash.poke.component;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import com.mongodb.BasicDBObject;
import com.noash.poke.dao.ItemDao;
import com.noash.poke.dao.PokemonDao;
import com.noash.poke.domain.Pokemon;
import com.noash.poke.pojo.dto.Progress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Slf4j
@Component
public class ResourceLoader {

    @Autowired
    private MongoTemplate mongo;
    @Autowired
    private PokemonDao pokemonDao;
    @Autowired
    private ItemDao itemDao;

    @Value("${pokemon-data.version}")
    private String version;
    @Value("${pokemon-data.dex-total}")
    private Integer dexTotal;
    @Value("${pokemon-data.url.pokemon-icon-prefix}")
    private String pokemonIconPrefix;
    @Value("${pokemon-data.url.pokemon-static-prefix}")
    private String pokemonStaticPrefix;
    @Value("${pokemon-data.url.item-icon-prefix}")
    private String itemIconPrefix;
    @Value("${pokemon-data.url.pokemon-cry-prefix}")
    private String pokemonCryPrefix;

    @Value("${oss.endpoint}")
    private String endpoint;
    @Value("${oss.bucket-name}")
    private String bucketName;
    @Value("${oss.access-key-id}")
    private String accessKeyId;
    @Value("${oss.access-key-secret}")
    private String accessKeySecret;

    void loadPokemonImages() throws Exception {
        log.info("----------LOAD pokemon images----------");
        // get progress
        Query query = new BasicQuery(new BasicDBObject("version", version).append("step", 4));
        Integer loadedNum = mongo.findOne(query, Progress.class).getNumber();
        log.info("Already loaded {} groups of records", loadedNum);

        // set oss client configurations
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("max-age=36000000");
        meta.setObjectAcl(CannedAccessControlList.PublicRead);

        // handle not loaded records
        List<Pokemon> pokemons = pokemonDao.selectNotLoaded(loadedNum);
        for (Pokemon pokemon : pokemons) {
            String natId;
            String key;
            if (pokemon.getSubId() == 0) {
                natId = key = pokemon.getNationalId().toString();
            } else {
                natId = pokemon.getNationalId() + "." + pokemon.getSubId();
                key = pokemon.getNationalId() + "-" + pokemon.getSubId();
            }

            log.info("Fetch No.{} icon", natId);
            HttpURLConnection connection = (HttpURLConnection) new URL(pokemonIconPrefix + natId + ".png").openConnection();
            connection.setUseCaches(false);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                log.info("Upload No.{} icon", key);
                ossClient.putObject(bucketName, "pokemon/icon/" + key + ".png", connection.getInputStream(), meta);
            }

            log.info("Fetch No.{} static image", natId);
            connection = (HttpURLConnection) new URL(pokemonStaticPrefix + natId + ".png").openConnection();
            connection.setUseCaches(false);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                log.info("Upload No.{} static image", key);
                ossClient.putObject(bucketName, "pokemon/static/" + key + ".png", connection.getInputStream(), meta);
            }

            mongo.updateFirst(query, Update.update("number", pokemon.getNationalId()), Progress.class);
        }

        ossClient.shutdown();
    }

    void loadItemImages() throws Exception {
        log.info("----------LOAD item images----------");
        // get progress
        Query query = new BasicQuery(new BasicDBObject("version", version).append("step", 5));
        Integer loadedNum = mongo.findOne(query, Progress.class).getNumber();
        log.info("Already loaded {} records", loadedNum);

        // set oss client configurations
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("max-age=36000000");
        meta.setObjectAcl(CannedAccessControlList.PublicRead);


        // handle not loaded records
        List<Integer> itemIds = itemDao.selectNotLoaded(loadedNum);
        for (Integer itemId : itemIds) {
            log.info("Fetch No.{} icon", itemId);
            HttpURLConnection connection = (HttpURLConnection) new URL(itemIconPrefix + itemId + ".png").openConnection();
            connection.setUseCaches(false);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                log.info("Upload No.{} icon", itemId);
                ossClient.putObject(bucketName, "pokemon/item/" + itemId + ".png", connection.getInputStream(), meta);
            }

            mongo.updateFirst(query, Update.update("number", itemId), Progress.class);
        }

        ossClient.shutdown();
    }

    void loadPokemonCries() throws Exception {
        log.info("----------LOAD pokemon cries----------");
        // get progress
        Query query = new BasicQuery(new BasicDBObject("version", version).append("step", 6));
        Integer loadedNum = mongo.findOne(query, Progress.class).getNumber();
        log.info("Already loaded {} records", loadedNum);

        // set oss client configurations
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("max-age=36000000");
        meta.setObjectAcl(CannedAccessControlList.PublicRead);
        meta.setContentType("audio/ogg");

        // handle not loaded records
        while (loadedNum < dexTotal) {
            log.info("Fetch No.{} cry", ++loadedNum);
            HttpURLConnection connection = (HttpURLConnection) new URL(pokemonCryPrefix + loadedNum + ".ogg").openConnection();
            connection.setUseCaches(false);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                log.info("Upload No.{} cry", loadedNum);
                ossClient.putObject(bucketName, "pokemon/cry/" + loadedNum + ".ogg", connection.getInputStream(), meta);
            }
            mongo.updateFirst(query, Update.update("number", loadedNum), Progress.class);
        }

        ossClient.shutdown();
    }

}
