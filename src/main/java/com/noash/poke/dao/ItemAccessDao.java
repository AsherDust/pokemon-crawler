package com.noash.poke.dao;

import com.noash.poke.domain.ItemAccess;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ItemAccessDao {

    String TABLE = "item_access";
    String COLUMNS = "item_id, type, access";

    @Insert({
        "INSERT INTO", TABLE,
        "(", COLUMNS, ")",
        "VALUES",
        "(#{itemId}, #{type}, #{access})"
    })
    int insert(ItemAccess itemAccess);
}
