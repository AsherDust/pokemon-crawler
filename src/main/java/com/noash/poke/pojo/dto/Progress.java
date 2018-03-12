package com.noash.poke.pojo.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@CompoundIndex(name = "version_1_step_1", def = "{'version': 1, 'step': 1}", unique = true)
public class Progress {

    private String version;

    private Integer step;

    private Integer number;
}
