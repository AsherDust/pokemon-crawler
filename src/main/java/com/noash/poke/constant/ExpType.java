package com.noash.poke.constant;

public enum ExpType {

    NONE(0, "-"),
    A(3, "1,000,000"),
    B(1, "600,000"),
    C(6, "1,640,000"),
    D(4, "1,059,860"),
    E(2, "800,000"),
    F(5, "1,250,000");

    private Integer code;
    private String value;

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    ExpType(Integer code, String value) {

        this.code = code;
        this.value = value;
    }

}
