package com.goblincwl.cwlweb.modules.app.jrebel.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Jrebel Leases Entity
 *
 * @author ☪wl
 * @email goblincwl@qq.com
 * @date 2022/12/05 11:22
 */
@Data
public class JrebelInfoEntity implements Serializable {
    private static final long serialVersionUID = 629909519356543832L;

    private String product;
    private String oldGuid;
    private String clientProtocolVersion;
    private String signature;
    private String guid;
    private String randomness;
    private String oldDefinedUserId;
    private String clientTime;
    private String rebelVersion;
    private String definedUserId;
    private String token;
    private String username;
    private String offline;
    private String offlineDays;

}
