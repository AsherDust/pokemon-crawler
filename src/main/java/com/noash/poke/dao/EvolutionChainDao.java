package com.noash.poke.dao;

import com.noash.poke.domain.EvolutionChain;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EvolutionChainDao {

    String TABLE = "evolution_chain";
    String COLUMNS = "base_id, second_id, third_id, second_descr, third_descr";

    @Insert({
        "INSERT IGNORE INTO", TABLE,
        "(", COLUMNS, ")",
        "VALUES",
        "(#{baseId}, #{secondId}, #{thirdId}, #{secondDescr}, #{thirdDescr})"
    })
    int insert(EvolutionChain evolutionChain);
}
