package com.goblincwl.cwlweb.manager.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.goblincwl.cwlweb.common.enums.BadWordType;
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

    /**
     * 主键
     */
    private Integer id;
    /**
     * 违禁词
     */
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY, condition = SqlCondition.LIKE)
    private String word;
    /**
     * 违禁词类型
     */
    private BadWordType type;

    /**
     * 违禁词类型-中文
     */
    public String getTypeName() {
        return this.type.getName();
    }
}
