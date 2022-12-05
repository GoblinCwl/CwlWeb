package com.goblincwl.cwlweb.modules.manager.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 键值对配置 Entity
 *
 * @author ☪wl
 * @date 2021-05-03 14:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyValueOptions implements Serializable {

    private static final long serialVersionUID = -4001377977682270212L;
    /**
     * 键
     */
    @TableId(type = IdType.INPUT)
    private String optKey;
    /**
     * 值
     */
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String optValue;

    /**
     * 备注
     */
    private String remark;
}
