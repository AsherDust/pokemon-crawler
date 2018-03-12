package com.noash.poke.dao;

import com.noash.poke.domain.Constant;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConstantDao {

    String TABLE = "constant";
    String COLUMNS = "type_name, code, value";

    @Insert({
        "INSERT INTO", TABLE,
        "(", COLUMNS, ")",
        "VALUES",
        "(#{typeName}, #{code}, #{value})"
    })
    int insert(Constant constant);

}
