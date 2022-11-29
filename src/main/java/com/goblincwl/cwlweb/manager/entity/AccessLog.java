package com.goblincwl.cwlweb.manager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 访问日志 Entitiy
 *
 * @author ☪wl
 * @email goblincwl@qq.com
 * @date 2022/11/28 09:02
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessLog implements Serializable {

    private static final long serialVersionUID = -5296014989863511738L;

    public AccessLog(Integer accessRecordId) {
        this.accessRecordId = accessRecordId;
    }

    /**
     * 主键
     */
    private Integer id;

    /**
     * 访问记录ID
     */
    private Integer accessRecordId;

    /**
     * 访问时间
     */
    private Date accessDate;
}
