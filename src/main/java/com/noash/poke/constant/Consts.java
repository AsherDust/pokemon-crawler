package com.noash.poke.constant;

import java.util.HashMap;
import java.util.Map;

public class Consts {

    public static final Map<Integer, Integer> TYPE_MAPPER = new HashMap<>();
    public static final Map<Integer, Integer> GENDER_MAPPER = new HashMap<>();

    static {
        TYPE_MAPPER.put(0, 0);
        TYPE_MAPPER.put(1, 2);
        TYPE_MAPPER.put(2, 3);
        TYPE_MAPPER.put(3, 4);
        TYPE_MAPPER.put(4, 5);
        TYPE_MAPPER.put(5, 1);
        TYPE_MAPPER.put(6, 7);
        TYPE_MAPPER.put(7, 10);
        TYPE_MAPPER.put(8, 12);
        TYPE_MAPPER.put(9, 8);
        TYPE_MAPPER.put(10, 13);
        TYPE_MAPPER.put(11, 9);
        TYPE_MAPPER.put(12, 17);
        TYPE_MAPPER.put(13, 6);
        TYPE_MAPPER.put(14, 11);
        TYPE_MAPPER.put(15, 16);
        TYPE_MAPPER.put(16, 14);
        TYPE_MAPPER.put(17, 15);
        TYPE_MAPPER.put(18, 18);

        GENDER_MAPPER.put(255, 0);
        GENDER_MAPPER.put(172, 0);
        GENDER_MAPPER.put(254, 1);
        GENDER_MAPPER.put(225, 2);
        GENDER_MAPPER.put(191, 3);
        GENDER_MAPPER.put(127, 4);
        GENDER_MAPPER.put(63, 5);
        GENDER_MAPPER.put(31, 6);
        GENDER_MAPPER.put(0, 7);
    }

}
