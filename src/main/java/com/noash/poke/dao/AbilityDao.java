package com.noash.poke.dao;

import com.noash.poke.domain.Ability;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AbilityDao {

    String TABLE = "ability";
    String COLUMNS = "id, name_zh, name_en, name_jp, generation, brief_descr, effect_descr";

    @Insert({
        "INSERT INTO", TABLE,
        "(", COLUMNS, ")",
        "VALUES",
        "(#{id}, #{nameZh}, #{nameEn}, #{nameJp}, #{generation}, #{briefDescr}, #{effectDescr})"
    })
    int insert(Ability ability);
}
