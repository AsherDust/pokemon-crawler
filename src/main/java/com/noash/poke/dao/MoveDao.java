package com.noash.poke.dao;

import com.noash.poke.domain.Move;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MoveDao {

    String TABLE = "move";
    String COLUMNS = "id, name_zh, name_en, name_jp, type_id, clazz, power, accuracy, pp, priority, z_power, z_effect, target, tags, descr";

    @Insert({
        "INSERT INTO", TABLE,
        "(", COLUMNS, ")",
        "VALUES",
        "(#{id}, #{nameZh}, #{nameEn}, #{nameJp}, #{typeId}, #{clazz}, #{power}, #{accuracy}, #{pp}, #{priority},",
        "#{zPower}, #{zEffect}, #{target}, #{tags}, #{descr})"
    })
    int insert(Move move);
}
