package com.noash.poke.dao;

import com.noash.poke.domain.Pokemon;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PokemonDao {

    @Insert({
        "INSERT INTO pokemon (national_id, sub_id,",
        "name_dex, name_zh, name_en,",
        "name_jp, type_a_id,",
        "type_b_id, ability_a_id,",
        "ability_b_id, ability_hidden_id,",
        "item_common_id, item_rare_id,",
        "race_hp, race_atk,",
        "race_def, race_spatk,",
        "race_spdef, race_spd,",
        "base_hp, base_atk,",
        "base_def, base_spatk,",
        "base_spdef, base_spd,",
        "category, color_type,",
        "height, weight,",
        "egg_group_a, egg_group_b,",
        "egg_cycle, gender_type,",
        "exp_type, capture_rate,",
        "happiness_initial, descr_sun,",
        "descr_moon, descr_us,",
        "descr_um)",
        "VALUES (#{nationalId}, #{subId},",
        "#{nameDex}, #{nameZh}, #{nameEn},",
        "#{nameJp}, #{typeAId},",
        "#{typeBId}, #{abilityAId}, ",
        "#{abilityBId}, #{abilityHiddenId},",
        "#{itemCommonId}, #{itemRareId},",
        "#{raceHp}, #{raceAtk},",
        "#{raceDef}, #{raceSpatk},",
        "#{raceSpdef}, #{raceSpd},",
        "#{baseHp}, #{baseAtk},",
        "#{baseDef}, #{baseSpatk},",
        "#{baseSpdef}, #{baseSpd},",
        "#{category}, #{colorType},",
        "#{height}, #{weight},",
        "#{eggGroupA}, #{eggGroupB},",
        "#{eggCycle}, #{genderType},",
        "#{expType}, #{captureRate},",
        "#{happinessInitial}, #{descrSun},",
        "#{descrMoon}, #{descrUs},",
        "#{descrUm})"
    })
    int insert(Pokemon record);

    @Select({
        "SELECT id, sub_id",
        "FROM pokemon",
        "WHERE national_id = #{nationalId}"
    })
    List<Pokemon> selectByNationalId(Integer nationalId);

    @Select({
        "SELECT id",
        "FROM pokemon",
        "WHERE national_id = #{nationalId}",
        "AND sub_id = #{subId}"
    })
    Integer selectIdByNationalIdAndSubId(@Param("nationalId") Integer nationalId, @Param("subId") Integer subId);

    @Select({
        "SELECT national_id, sub_id",
        "FROM pokemon",
        "WHERE national_id > #{fetchedNum}"
    })
    List<Pokemon> selectNotLoaded(Integer loadedNum);

}
