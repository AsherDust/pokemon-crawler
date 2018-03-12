package com.noash.poke.constant;

public enum MoveVersion {

    SM("7", 8), USUM("7-1", 9);

    private String key;
    private Integer version;

    MoveVersion(String key, Integer version) {
        this.key = key;
        this.version = version;
    }

    public String getKey() {
        return key;
    }

    public Integer getVersion() {
        return version;
    }

}
