package com.goblincwl.cwlweb.index.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 访问记录 Entity
 *
 * @author ☪wl
 * @date 2021-04-24 18:33
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessRecord implements Serializable {

    private static final long serialVersionUID = -4090579113660046062L;
    /**
     * 主键
     */
    private Integer id;
    /**
     * 记录IP地址
     */
    private String ipAddress;
    /**
     * 记录用户昵称
     */
    private String nickName;
    /**
     * 访问时间
     */
    private Date accessTime;
    /**
     * 上次访问时间
     */
    private Date lastAccessTime;
    /**
     * 访问总次数
     */
    private Integer accessCount;
}
