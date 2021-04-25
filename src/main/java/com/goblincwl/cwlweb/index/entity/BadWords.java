package com.goblincwl.cwlweb.index.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 敏感词 Entity
 *
 * @author ☪wl
 * @date 2021-04-25 14:32
 */
@Data
public class BadWords implements Serializable {
    private static final long serialVersionUID = 2804468775639468086L;

    private Integer id;
    private String word;
    private String type;
}
