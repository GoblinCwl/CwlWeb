package com.goblincwl.cwlweb.modules.app.apps.jrebel.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ☪wl
 * @email goblincwl@qq.com
 * @date 2022/12/05 14:35
 */
@Data
public class XMLResponse implements Serializable {
    private static final long serialVersionUID = 5676828258469752234L;

    private String message;
    private String prolongationPeriod;
    private String responseCode;
    private String salt;
    private String ticketId;
    private String ticketProperties;
}
