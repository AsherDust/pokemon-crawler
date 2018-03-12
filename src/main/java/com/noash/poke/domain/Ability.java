package com.noash.poke.domain;

import lombok.Data;

@Data
public class Ability {

    private Integer id;

    private String nameZh;

    private String nameEn;

    private String nameJp;

    private Integer generation;

    private String briefDescr;

    private String effectDescr;
}
