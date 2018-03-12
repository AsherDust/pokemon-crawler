package com.noash.poke.dao;

import com.noash.poke.domain.Item;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ItemDao {

    String TABLE = "item";
    String COLUMNS = "id, name_zh, name_en, name_jp, type, price, is_usable, is_disposable, tm_move_id, descr";

    @Insert({
        "INSERT INTO", TABLE,
        "(", COLUMNS, ")",
        "VALUES",
        "(#{id}, #{nameZh}, #{nameEn}, #{nameJp}, #{type}, #{price}, #{isUsable}, #{isDisposable}, #{tmMoveId}, #{descr})"
    })
    int insert(Item item);
}
