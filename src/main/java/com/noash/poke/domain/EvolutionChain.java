package com.noash.poke.domain;

import lombok.Data;

@Data
public class EvolutionChain {

    private Integer id;

    private Integer baseId;

    private Integer secondId;

    private Integer thirdId;

    private String secondDescr;

    private String thirdDescr;

}
