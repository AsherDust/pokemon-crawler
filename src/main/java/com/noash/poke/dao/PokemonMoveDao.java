package com.noash.poke.dao;

import com.noash.poke.domain.PokemonMove;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PokemonMoveDao {

    @Insert({
        "INSERT INTO pokemon_move_level",
        "(version, pokemon_id, move_id, learn_level)",
        "VALUES",
        "(#{version}, #{pokemonId}, #{moveId}, #{learnLevel})"
    })
    int insertLevel(PokemonMove pokemonMove);

    @Insert({
        "INSERT INTO pokemon_move_tm",
        "(version, pokemon_id, move_id, tm)",
        "VALUES",
        "(#{version}, #{pokemonId}, #{moveId}, #{tm})"
    })
    int insertTm(PokemonMove pokemonMove);

    @Insert({
        "INSERT INTO pokemon_move_fixed",
        "(version, pokemon_id, move_id)",
        "VALUES",
        "(#{version}, #{pokemonId}, #{moveId})"
    })
    int insertFixed(PokemonMove pokemonMove);

}
