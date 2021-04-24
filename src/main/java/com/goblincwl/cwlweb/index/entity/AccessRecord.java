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
    private Integer id;
    private String ipAddress;
    private String nickName;
    private Date accessTime;
    private Date lastAccessTime;
    private Integer accessCount;

}
