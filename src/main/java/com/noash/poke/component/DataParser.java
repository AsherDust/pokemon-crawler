package com.noash.poke.component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.noash.poke.constant.ExpType;
import com.noash.poke.constant.MoveVersion;
import com.noash.poke.constant.PokemonField;
import com.noash.poke.dao.*;
import com.noash.poke.domain.*;
import com.noash.poke.pojo.dto.Progress;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.noash.poke.constant.Consts.GENDER_MAPPER;
import static com.noash.poke.constant.Consts.TYPE_MAPPER;

@Slf4j
@Component
public class DataParser {

    @Autowired
    private PokemonDao pokemonDao;
    @Autowired
    private MongoTemplate mongo;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Value("${pokemon-data.version}")
    private String version;
    @Value("${pokemon-data.dex-total}")
    private int dexTotal;

    @Transactional
    public void parseAbilities() {
        log.info("----------PARSE abilities----------");
        String collection = "ability_" + version;
        String[] keys = {"abi_id", "name_zh", "name_en", "name_jp", "generation", "brief_description", "effect_description"};
        Function<JSONObject, Ability> mapper = json -> {
            Ability ability = new Ability();
            ability.setId(json.getInteger(keys[0]));
            ability.setNameZh(json.getString(keys[1]));
            ability.setNameEn(json.getString(keys[2]));
            ability.setNameJp(json.getString(keys[3]));
            ability.setGeneration(json.getInteger(keys[4]));
            ability.setBriefDescr(json.getString(keys[5]));
            ability.setEffectDescr(json.getString(keys[6]));

            return ability;
        };

        List<Ability> abilities = parseDataToDomainObjects(collection, keys, mapper);

        log.info("Batch insert abilities to DB");
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            AbilityDao abilityDao = sqlSession.getMapper(AbilityDao.class);
            abilities.forEach(abilityDao::insert);
            sqlSession.commit();
        }
    }

    @Transactional
    public void parseItems() {
        log.info("----------PARSE items----------");
        String collection = "item_" + version;
        String[] keys = {"item_id", "name_zh", "name_en", "name_jp", "type", "price", "is_usable", "is_one_time", "tm_id", "description"};
        Function<JSONObject, Item> mapper = json -> {
            Item item = new Item();
            item.setId(json.getInteger(keys[0]));
            item.setNameZh(json.getString(keys[1]));
            item.setNameEn(json.getString(keys[2]));
            item.setNameJp(json.getString(keys[3]));
            item.setType(json.getInteger(keys[4]));
            item.setPrice(json.getInteger(keys[5]));
            item.setIsUsable(json.getBoolean(keys[6]));
            item.setIsDisposable(json.getBoolean(keys[7]));
            item.setTmMoveId(json.getInteger(keys[8]));
            item.setDescr(json.getString(keys[9]));

            return item;
        };

        List<Item> items = parseDataToDomainObjects(collection, keys, mapper);

        log.info("Batch insert items to DB");
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            ItemDao itemDao = sqlSession.getMapper(ItemDao.class);
            items.forEach(itemDao::insert);
            sqlSession.commit();
        }
    }

    @Transactional
    public void parseItemAccesses() {
        log.info("----------PARSE item accesses----------");
        String collection = "item_" + version;
        String[] keys = {"item_id", "obtain_approaches"};

        log.info("Get data from {}", collection);
        DBObject queryObject = new BasicDBObject();
        DBObject fieldsObject = new BasicDBObject("_id", false);
        for (String key : keys) {
            fieldsObject.put(key, true);
        }

        List<JSONObject> jsonList = mongo.find(new BasicQuery(queryObject, fieldsObject), JSONObject.class, collection);
        log.info("{} records got in all", jsonList.size());

        log.info("Parse data to domain objects");
        List<ItemAccess> itemAccesses = new ArrayList<>();
        for (JSONObject json : jsonList) {
            Integer itemId = json.getInteger(keys[0]);
            JSONArray obtainApproaches = json.getJSONArray(keys[1]);
            for (Object obtainApproach : obtainApproaches) {
                Object type = ((JSONArray) obtainApproach).get(0);
                Object access = ((JSONArray) obtainApproach).get(1);
                if (type != null && access != null) {
                    ItemAccess itemAccess = new ItemAccess();
                    itemAccess.setItemId(itemId);
                    itemAccess.setType(type.toString());
                    itemAccess.setAccess(access.toString());

                    itemAccesses.add(itemAccess);
                }
            }
        }

        log.info("Batch insert item accesses to DB");
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            ItemAccessDao itemAccessDao = sqlSession.getMapper(ItemAccessDao.class);
            itemAccesses.forEach(itemAccessDao::insert);
            sqlSession.commit();
        }
    }

    @Transactional
    public void parseMoves() {
        log.info("----------PARSE moves----------");
        String collection = "move_" + version;
        String[] keys = {"move_id", "name_zh", "name_en", "name_jp", "type", "clazz", "power", "accuracy", "pp",
            "priority", "z_power", "z_effect", "target", "flags", "description"};
        Function<JSONObject, Move> mapper = json -> {
            Move move = new Move();
            move.setId(json.getInteger(keys[0]));
            move.setNameZh(json.getString(keys[1]));
            move.setNameEn(json.getString(keys[2]));
            move.setNameJp(json.getString(keys[3]));
            move.setTypeId(TYPE_MAPPER.get(json.getInteger(keys[4])));
            move.setClazz(json.getInteger(keys[5]));
            move.setPower(json.getInteger(keys[6]));
            move.setAccuracy(json.getInteger(keys[7]));
            move.setPp(json.getInteger(keys[8]));
            move.setPriority(json.getInteger(keys[9]));
            move.setZPower(json.getInteger(keys[10]));
            move.setZEffect(json.getInteger(keys[11]));
            move.setTarget(json.getInteger(keys[12]));
            move.setTags(json.getString(keys[13]));
            move.setDescr(json.getString(keys[14]));

            return move;
        };

        List<Move> moves = parseDataToDomainObjects(collection, keys, mapper);

        log.info("Batch insert moves to DB");
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            MoveDao moveDao = sqlSession.getMapper(MoveDao.class);
            moves.forEach(moveDao::insert);
            sqlSession.commit();
        }
    }

    @Transactional
    public void parsePokedexIds() {
        log.info("----------PARSE pokedex ids----------");
        String collection = "pokemon_" + version;
        String[] keys = {"nat_id", "alola_id_sm", "melemele_id_sm", "akala_id_sm", "ulaula_id_sm", "poni_id_sm",
            "alola_id", "melemele_id", "akala_id", "ulaula_id", "poni_id"};
        Function<JSONObject, PokedexId> mapper = json -> new PokedexId(
            json.getInteger(keys[0]),
            json.getInteger(keys[1]),
            json.getInteger(keys[2]),
            json.getInteger(keys[3]),
            json.getInteger(keys[4]),
            json.getInteger(keys[5]),
            json.getInteger(keys[6]),
            json.getInteger(keys[7]),
            json.getInteger(keys[8]),
            json.getInteger(keys[9]),
            json.getInteger(keys[10])
        );

        List<PokedexId> pokedexIds = parseDataToDomainObjects(collection, keys, mapper);

        log.info("Batch insert pokedex ids to DB");
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            PokedexIdDao pokedexIdDao = sqlSession.getMapper(PokedexIdDao.class);
            pokedexIds.forEach(pokedexIdDao::insert);
            sqlSession.commit();
        }
    }

    private <T> List<T> parseDataToDomainObjects(String collection, String[] keys, Function<JSONObject, T> mapper) {
        log.info("Get data from {}", collection);
        DBObject queryObject = new BasicDBObject();
        DBObject fieldsObject = new BasicDBObject("_id", false);
        for (String key : keys) {
            fieldsObject.put(key, true);
        }

        List<JSONObject> jsonList = mongo.find(new BasicQuery(queryObject, fieldsObject), JSONObject.class, collection);
        log.info("{} records got in all", jsonList.size());

        log.info("Parse data to domain objects");

        return jsonList.stream().map(mapper).collect(Collectors.toList());
    }

    @Transactional
    public void parsePokemons() {
        log.info("----------PARSE pokemons----------");
        // set field projections for target collection
        String collection = "pokemon_" + version;
        String innerKey = "forme";

        DBObject fieldsObject = new BasicDBObject("_id", false);
        for (PokemonField pokemonField : PokemonField.values()) {
            fieldsObject.put(pokemonField.getValue(), true);
            if (pokemonField.isInFrome()) {
                fieldsObject.put(innerKey + "." + pokemonField.getValue(), true);
            }
        }

        // parse data to domain objects
        List<Pokemon> pokemons = new ArrayList<>();
        for (int i = 1; i < dexTotal + 1; i++) {
            log.info("Get No.{} data", i);
            DBObject queryObject = new BasicDBObject("nat_id", i);
            JSONObject json = mongo.findOne(new BasicQuery(queryObject, fieldsObject), JSONObject.class, collection);

            log.info("json: {}", json);

            Pokemon pokemon = new Pokemon();
            parsePokemonOuterData(json, pokemon);
            parsePokemonInnerData(json, pokemon);
            pokemons.add(pokemon);
            log.info("pokemon: {}", pokemon);

            // parse data in forme
            JSONArray forme = json.getJSONArray(innerKey);
            for (Object data : forme) {
                pokemon = new Pokemon();
                parsePokemonOuterData(json, pokemon);
                parsePokemonInnerData((JSONObject) data, pokemon);
                pokemons.add(pokemon);
                log.info("pokemon: {}", pokemon);
            }
        }

        log.info("Batch insert pokemons to DB");
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            PokemonDao pokemonDao = sqlSession.getMapper(PokemonDao.class);
            pokemons.forEach(pokemonDao::insert);
            sqlSession.commit();
        }
    }

    private void parsePokemonOuterData(JSONObject json, Pokemon pokemon) {
        pokemon.setNameDex(json.getString(PokemonField.NAME.getValue()));
        pokemon.setNameEn(json.getString(PokemonField.NAME_EN.getValue()));
        pokemon.setNameJp(json.getString(PokemonField.NAME_JP.getValue()));
        pokemon.setCategory(json.getString(PokemonField.CATEGORY.getValue()));
        pokemon.setColorType(json.getInteger(PokemonField.COLOR.getValue()));
        pokemon.setDescrUs(json.getString(PokemonField.DESCRIPTION_US.getValue()));
        pokemon.setDescrUm(json.getString(PokemonField.DESCRIPTION_UM.getValue()));
    }

    private void parsePokemonInnerData(JSONObject json, Pokemon pokemon) {
        pokemon.setNameZh(json.getString(PokemonField.NAME_ZH.getValue()));
        pokemon.setTypeAId(TYPE_MAPPER.get(json.getInteger(PokemonField.TYPE.getValue())));
        pokemon.setTypeBId(TYPE_MAPPER.get(json.getInteger(PokemonField.TYPE_B.getValue())));
        pokemon.setAbilityAId(json.getInteger(PokemonField.ABILITY.getValue()));
        pokemon.setAbilityBId(json.getInteger(PokemonField.ABILITY_B.getValue()));
        pokemon.setAbilityHiddenId(json.getInteger(PokemonField.ABILITY_HIDDEN.getValue()));
        pokemon.setHeight(json.getBigDecimal(PokemonField.HEIGHT.getValue()));
        pokemon.setWeight(json.getBigDecimal(PokemonField.WEIGHT.getValue()));
        pokemon.setEggGroupA(json.getInteger(PokemonField.EGG_GROUP.getValue()));
        pokemon.setEggGroupB(json.getInteger(PokemonField.EGG_GROUP_B.getValue()));
        pokemon.setEggCycle(json.getInteger(PokemonField.EGG_CYCLE.getValue()));
        pokemon.setGenderType(GENDER_MAPPER.get(json.getInteger(PokemonField.GENDER_RATE.getValue())));
        pokemon.setExpType(json.getInteger(PokemonField.EXP_TYPE.getValue()));
        pokemon.setCaptureRate(json.getInteger(PokemonField.CAPTURE_RATE.getValue()));
        pokemon.setHappinessInitial(json.getInteger(PokemonField.HAPPINESS_INITIAL.getValue()));
        pokemon.setDescrSun(json.getString(PokemonField.DESCRIPTION_SUN.getValue()));
        pokemon.setDescrMoon(json.getString(PokemonField.DESCRIPTION_MOON.getValue()));
        // parse nat_id to nationalId and subId
        String[] ids = json.getString(PokemonField.NAT_ID.getValue()).split("\\.");
        if (ids.length == 2) {
            pokemon.setNationalId(Integer.valueOf(ids[0]));
            pokemon.setSubId(Integer.valueOf(ids[1]));
        } else {
            pokemon.setNationalId(Integer.valueOf(ids[0]));
            pokemon.setSubId(0);
        }
        // parse hold_item_<*> to item_<*>_id
        JSONObject holdItemCommon = json.getJSONObject((PokemonField.HOLD_ITEM_COMMON.getValue()));
        if (holdItemCommon != null) {
            pokemon.setItemCommonId(holdItemCommon.getInteger("item_id"));
        } else {
            pokemon.setItemCommonId(0);
        }
        JSONObject holdItemRare = json.getJSONObject((PokemonField.HOLD_ITEM_RARE.getValue()));
        if (holdItemRare != null) {
            pokemon.setItemRareId(holdItemRare.getInteger("item_id"));
        } else {
            pokemon.setItemRareId(0);
        }
        // parse base_stat to race_<*>
        JSONArray baseStat = json.getJSONArray(PokemonField.BASE_STAT.getValue());
        pokemon.setRaceHp((Integer) baseStat.get(0));
        pokemon.setRaceAtk((Integer) baseStat.get(1));
        pokemon.setRaceDef((Integer) baseStat.get(2));
        pokemon.setRaceSpatk((Integer) baseStat.get(3));
        pokemon.setRaceSpdef((Integer) baseStat.get(4));
        pokemon.setRaceSpd((Integer) baseStat.get(5));
        // parse effort_value to base_<*>
        JSONArray effortValue = json.getJSONArray(PokemonField.EFFORT_VALUE.getValue());
        pokemon.setBaseHp((Integer) effortValue.get(0));
        pokemon.setBaseAtk((Integer) effortValue.get(1));
        pokemon.setBaseDef((Integer) effortValue.get(2));
        pokemon.setBaseSpatk((Integer) effortValue.get(3));
        pokemon.setBaseSpdef((Integer) effortValue.get(4));
        pokemon.setBaseSpd((Integer) effortValue.get(5));
    }

    void parsePokemonLevelMoves() {
        parsePokemonMoves(1);
    }

    void parsePokemonTmMoves() {
        parsePokemonMoves(2);
    }

    void parsePokemonFixedMoves() {
        parsePokemonMoves(3);
    }

    private void parsePokemonMoves(Integer type) {
        String label;
        switch (type) {
            case 1:
                label = "level";
                break;
            case 2:
                label = "tm";
                break;
            case 3:
                label = "fixed";
                break;
            default:
                return;
        }
        log.info("----------PARSE pokemon {}-moves----------", label);
        String collection = "pokemon_" + version;
        String[] keys = {"move_id", "learn_level", "gen7_tm_id"};
        DBObject fieldsObject = new BasicDBObject("_id", false);
        fieldsObject.put("forme.nat_id", true);
        for (MoveVersion moveVersion : MoveVersion.values()) {
            for (String key : keys) {
                String suffix = moveVersion.getKey() + "." + type + "." + key;
                fieldsObject.put("moves." + suffix, true);
                fieldsObject.put("forme.moves." + suffix, true);
            }
        }

        Query query = new BasicQuery(new BasicDBObject("version", version).append("step", type));
        int parsedNum = mongo.findOne(query, Progress.class).getNumber();
        log.info("Already parsed {} records", parsedNum);

        while (parsedNum < dexTotal) {
            log.info("Get No.{} {}-move data", ++parsedNum, label);
            DBObject queryObject = new BasicDBObject("nat_id", parsedNum);
            JSONObject json = mongo.findOne(new BasicQuery(queryObject, fieldsObject), JSONObject.class, collection);
            JSONArray formeData = json.getJSONArray("forme");
            List<Pokemon> pokemons = pokemonDao.selectByNationalId(parsedNum);

            List<PokemonMove> pokemonMoves = new ArrayList<>();
            for (Pokemon pokemon : pokemons) {
                if (pokemon.getSubId() == 0) {
                    convertDataAddToPokemonMoves(json, type, pokemon.getId(), pokemonMoves);
                } else {
                    for (Object data : formeData) {
                        JSONObject innerJson = (JSONObject) data;
                        String subId = innerJson.getString("nat_id").split("\\.")[1];
                        if (pokemon.getSubId().toString().equals(subId)) {
                            convertDataAddToPokemonMoves(innerJson, type, pokemon.getId(), pokemonMoves);
                        }
                    }
                }
            }

            batchInsertPokemonMoves(pokemonMoves, type, query, parsedNum);

            log.info("No.{} {}-move data parsed successfully! Total {} records", parsedNum, label, pokemonMoves.size());
        }
    }

    private void convertDataAddToPokemonMoves(JSONObject json, Integer type, Integer pokemonId, List<PokemonMove> pokemonMoves) {
        if (json.get("moves") instanceof JSONArray) {
            return;
        }

        for (MoveVersion moveVersion : MoveVersion.values()) {
            Optional.of(json.getJSONObject("moves"))
                .map(j -> j.getJSONObject(moveVersion.getKey()))
                .map(j -> j.getJSONArray(type.toString()))
                .ifPresent(j -> {
                    for (Object o : j) {
                        PokemonMove pokemonMove = new PokemonMove();
                        pokemonMove.setVersion(moveVersion.getVersion());
                        pokemonMove.setPokemonId(pokemonId);
                        pokemonMove.setMoveId(((JSONObject) o).getInteger("move_id"));
                        pokemonMove.setLearnLevel(((JSONObject) o).getInteger("learn_level"));
                        pokemonMove.setTm(((JSONObject) o).getInteger("gen7_tm_id"));

                        pokemonMoves.add(pokemonMove);
                    }
                });
        }
    }

    @Transactional
    public void batchInsertPokemonMoves(List<PokemonMove> pokemonMoves, Integer type, Query query, int parsedNum) {
        log.info("Batch insert pokemon level-moves to DB");
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            PokemonMoveDao pokemonMoveDao = sqlSession.getMapper(PokemonMoveDao.class);
            switch (type) {
                case 1:
                    pokemonMoves.forEach(pokemonMoveDao::insertLevel);
                    break;
                case 2:
                    pokemonMoves.forEach(pokemonMoveDao::insertTm);
                    break;
                case 3:
                    pokemonMoves.forEach(pokemonMoveDao::insertFixed);
                    break;
                default:
                    return;
            }
            sqlSession.commit();
        }

        mongo.updateFirst(query, Update.update("number", parsedNum), Progress.class);
    }

    @Transactional
    public void parseConstants() {
        log.info("----------PARSE constants----------");
        String collection = "constants_" + version;
        JSONObject json = mongo.findOne(new BasicQuery("{}"), JSONObject.class, collection);
        List<Constant> constants = new ArrayList<>();

        /*------ item ------*/
        convertDataAddToConstants(json.getJSONObject("item").getJSONArray("data_type"), "itemType", constants);

        /*------ move ------*/
        JSONObject move = json.getJSONObject("move");
        // move clazz
        convertDataAddToConstants(move.getJSONArray("data_class"), "moveClazz", constants);
        // z effect
        convertDataAddToConstants(move.getJSONArray("z_status_effects"), "zEffect", constants);
        // move target
        convertDataAddToConstants(move.getJSONArray("data_target"), "moveTarget", constants);
        // move tag
        convertDataAddToConstants(move.getJSONArray("data_flags"), "moveTag", constants);

        /*------ pokemon ------*/
        JSONObject pokemon = json.getJSONObject("pokemon");
        // egg group
        convertDataAddToConstants(pokemon.getJSONArray("data_egg_group"), "eggGroup", constants);
        // color type
        convertDataAddToConstants(pokemon.getJSONArray("data_color"), "colorType", constants);
        // gender type
        pokemon.getJSONObject("data_gender_ratio").forEach((k, v) -> {
            Constant constant = new Constant();
            constant.setTypeName("genderType");
            constant.setCode(GENDER_MAPPER.get(Integer.valueOf(k)));
            constant.setValue(v.toString());

            constants.add(constant);
        });
        // exp type
        for (ExpType expType : ExpType.values()) {
            Constant constant = new Constant();
            constant.setTypeName("expType");
            constant.setCode(expType.getCode());
            constant.setValue(expType.getValue());

            constants.add(constant);
        }


        log.info("Batch insert constants to DB");
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            ConstantDao constantDao = sqlSession.getMapper(ConstantDao.class);
            constants.forEach(constantDao::insert);
            sqlSession.commit();
        }
    }

    private void convertDataAddToConstants(JSONArray itemTypes, String typeName, List<Constant> constants) {
        for (int i = 0; i < itemTypes.size(); i++) {
            Constant constant = new Constant();
            constant.setTypeName(typeName);
            constant.setCode(i);
            constant.setValue(itemTypes.getString(i));

            constants.add(constant);
        }
    }

    @Transactional
    public void parseEvolutionChains() {
        log.info("----------PARSE evolution chain----------");
        String collection = "pokemon_" + version;

        // find all base national ids
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.group("devolution.nat_id").first("devolution.nat_id").as("base_id"));
        List<Integer> baseIds = mongo.aggregate(aggregation, collection, JSONObject.class)
            .getMappedResults().stream()
            .map(d -> d.getInteger("base_id"))
            .sorted()
            .collect(Collectors.toList());

        // parse data to domain objects
        DBObject fieldsObject = new BasicDBObject("_id", false)
            .append("devolution", true)
            .append("evolve_chain", true)
            .append("forme.devolution", true)
            .append("forme.evolve_chain", true);

        List<EvolutionChain> chains = new ArrayList<>();
        for (Integer baseId : baseIds) {
            log.info("Get No.{} data", baseId);
            DBObject queryObject = new BasicDBObject("nat_id", baseId);
            JSONObject json = mongo.findOne(new BasicQuery(queryObject, fieldsObject), JSONObject.class, collection);

            log.info("json: {}", json);

            parseChainData(json, chains);

            // parse data in forme
            JSONArray forme = json.getJSONArray("forme");
            for (Object data : forme) {
                parseChainData((JSONObject) data, chains);
            }
        }

        log.info("Batch insert evolution chains to DB");
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            EvolutionChainDao evolutionChainDao = sqlSession.getMapper(EvolutionChainDao.class);
            chains.forEach(evolutionChainDao::insert);
            sqlSession.commit();
        }
    }

    private void parseChainData(JSONObject json, List<EvolutionChain> chains) {
        Integer baseId = convertToPokemonId(json.getJSONObject("devolution").getString("nat_id"));

        JSONArray firstChains = json.getJSONArray("evolve_chain");
        for (Object firstChain : firstChains) {
            JSONObject firstApproach = ((JSONObject) firstChain).getJSONObject("approach");
            Integer secondId = convertToPokemonId(firstApproach.getString("0"));
            String secondDescr = firstApproach.getString("text");

            JSONArray secondChains = ((JSONObject) firstChain).getJSONArray("chain");
            if (secondChains.isEmpty()) {
                EvolutionChain chain = new EvolutionChain();
                chain.setBaseId(baseId);
                chain.setSecondId(secondId);
                chain.setSecondDescr(secondDescr);
                chain.setThirdId(0);
                chain.setThirdDescr("");

                chains.add(chain);
            } else {
                for (Object secondChain : secondChains) {
                    JSONObject secondApproach = ((JSONObject) secondChain).getJSONObject("approach");
                    Integer thirdId = convertToPokemonId(secondApproach.getString("0"));
                    String thirdDescr = secondApproach.getString("text");

                    EvolutionChain chain = new EvolutionChain();
                    chain.setBaseId(baseId);
                    chain.setSecondId(secondId);
                    chain.setSecondDescr(secondDescr);
                    chain.setThirdId(thirdId);
                    chain.setThirdDescr(thirdDescr);

                    chains.add(chain);
                }
            }
        }
    }

    private Integer convertToPokemonId(String natId) {
        String[] compoundId = natId.split("\\.");
        Integer subId = 0;
        if (compoundId.length == 2) {
            subId = Integer.valueOf(compoundId[1]);
        }

        return pokemonDao.selectIdByNationalIdAndSubId(Integer.valueOf(compoundId[0]), subId);
    }

    @Transactional
    public void repairAlolaIds() {
        log.info("----------PARSE alola ids----------");
        // set field projections for target collection
        String collection = "pokemon_" + version;
        String innerKey = "forme";

        DBObject fieldsObject = new BasicDBObject("_id", false)
            .append("nat_id", true)
            .append("alola_id", true)
            .append("forme.nat_id", true)
            .append("forme.alola_id", true);

        // parse data and update records
        for (int i = 1; i < dexTotal + 1; i++) {
            log.info("Get No.{} data", i);
            DBObject queryObject = new BasicDBObject("nat_id", i);
            JSONObject json = mongo.findOne(new BasicQuery(queryObject, fieldsObject), JSONObject.class, collection);

            parseAndUpdateAlolaId(json);
            // parse data in forme
            JSONArray forme = json.getJSONArray(innerKey);
            for (Object data : forme) {
                parseAndUpdateAlolaId((JSONObject) data);
            }
        }
    }

    private void parseAndUpdateAlolaId(JSONObject json) {
        Integer alolaId = json.getInteger("alola_id");
        if (alolaId != 0) {
            String[] compoundId = json.getString("nat_id").split("\\.");
            Integer nationalId = Integer.valueOf(compoundId[0]);
            int count = pokemonDao.countHasAlolaIdByNationalId(nationalId);
            if (count == 0) {
                Integer subId = 0;
                if (compoundId.length == 2) {
                    subId = Integer.valueOf(compoundId[1]);
                }
                pokemonDao.updateAlolaId(nationalId, subId, alolaId);
            }
        }
    }

}
