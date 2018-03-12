package com.noash.poke.domain;

import lombok.Data;

@Data
public class Move {

    private Integer id;

    private String nameZh;

    private String nameEn;

    private String nameJp;

    private Integer typeId;

    private Integer clazz;

    private Integer power;

    private Integer accuracy;

    private Integer pp;

    private Integer priority;

    private Integer zPower;

    private Integer zEffect;

    private Integer target;

    private String tags;

    private String descr;
}
