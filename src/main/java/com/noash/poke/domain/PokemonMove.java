package com.noash.poke.domain;

import lombok.Data;

@Data
public class PokemonMove {

    private Integer id;

    private Integer version;

    private Integer pokemonId;

    private Integer moveId;

    private Integer learnLevel;

    private Integer tm;

}
