package com.noash.poke.constant;

public enum PokemonField {
    NAME("name", false),
    NAME_EN("name_en", false),
    NAME_JP("name_jp", false),
    CATEGORY("category", false),
    COLOR("color", false),
    DESCRIPTION_US("description_us", false),
    DESCRIPTION_UM("description_um", false),

    NAT_ID("nat_id", true),
    NAME_ZH("name_zh", true),
    TYPE("type", true),
    TYPE_B("type_b", true),
    ABILITY("ability", true),
    ABILITY_B("ability_b", true),
    ABILITY_HIDDEN("ability_hidden", true),
    HOLD_ITEM_COMMON("hold_item_common", true),
    HOLD_ITEM_RARE("hold_item_rare", true),
    BASE_STAT("base_stat", true),
    EFFORT_VALUE("effort_value", true),
    HEIGHT("height", true),
    WEIGHT("weight", true),
    EGG_GROUP("egg_group", true),
    EGG_GROUP_B("egg_group_b", true),
    EGG_CYCLE("egg_cycle", true),
    GENDER_RATE("gender_rate", true),
    EXP_TYPE("exp_type", true),
    CAPTURE_RATE("capture_rate", true),
    HAPPINESS_INITIAL("happiness_initial", true),
    DESCRIPTION_SUN("description_sun", true),
    DESCRIPTION_MOON("description_moon", true);

    private String value;
    private boolean inFrome;

    PokemonField(String value, boolean inForme) {
        this.value = value;
        this.inFrome = inForme;
    }

    public String getValue() {
        return value;
    }

    public boolean isInFrome() {
        return inFrome;
    }

}
