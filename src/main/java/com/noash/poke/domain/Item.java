package com.noash.poke.domain;

import lombok.Data;

@Data
public class Item {

    private Integer id;

    private String nameZh;

    private String nameEn;

    private String nameJp;

    private Integer type;

    private Integer price;

    private Boolean isUsable;

    private Boolean isDisposable;

    private Integer tmMoveId;

    private String descr;
}
