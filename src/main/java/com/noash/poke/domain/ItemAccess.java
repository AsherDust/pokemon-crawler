package com.noash.poke.domain;

import lombok.Data;

@Data
public class ItemAccess {

    private Integer id;

    private Integer itemId;

    private String type;

    private String access;
}
