package com.goblincwl.cwlweb.modules.app.entitiy;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 应用 Entity
 *
 * @author ☪wl
 * @email goblincwl@qq.com
 * @date 2022/12/05 16:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class App implements Serializable {

    private static final long serialVersionUID = -8399153267293784055L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 应用名称
     */
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY, condition = SqlCondition.LIKE)
    private String name;

    /**
     * 应用地址
     */
    private String html;

    /**
     * 应用描述
     */
    private String description;

    /**
     * 是否锁定(0.开放,1.锁定)
     */
    private Integer isLock;

    /**
     * 是否锁定(查询条件)
     */
    @TableField(exist = false)
    private String isLockStr;

    /**
     * 使用次数
     */
    private Long usesTimes;

    /**
     * 图标URL
     */
    private String iconFile;

    /**
     * 图片地址
     */
    @TableField(exist = false)
    private String iconUrl;

    /**
     * 图片文件名
     */
    @TableField(exist = false)
    private String iconFileName;

    /**
     * 边框/文字颜色
     */
    private String color;

}
