package com.noash.poke.pojo.dto;

import lombok.Data;

import java.util.List;

@Data
public class KeyAndList {

    /** 索引键 */
    private String key;

    /** 原数据 */
    private List<Object> list;
}
