package com.noash.poke.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pokemon {

    private Integer id;

    private Integer nationalId;

    private Integer subId;

    private String nameDex;

    private String nameZh;

    private String nameEn;

    private String nameJp;

    private Integer typeAId;

    private Integer typeBId;

    private Integer abilityAId;

    private Integer abilityBId;

    private Integer abilityHiddenId;

    private Integer itemCommonId;

    private Integer itemRareId;

    private Integer raceHp;

    private Integer raceAtk;

    private Integer raceDef;

    private Integer raceSpatk;

    private Integer raceSpdef;

    private Integer raceSpd;

    private Integer baseHp;

    private Integer baseAtk;

    private Integer baseDef;

    private Integer baseSpatk;

    private Integer baseSpdef;

    private Integer baseSpd;

    private String category;

    private Integer colorType;

    private BigDecimal height;

    private BigDecimal weight;

    private Integer eggGroupA;

    private Integer eggGroupB;

    private Integer eggCycle;

    private Integer genderType;

    private Integer expType;

    private Integer captureRate;

    private Integer happinessInitial;

    private String descrSun;

    private String descrMoon;

    private String descrUs;

    private String descrUm;

}
