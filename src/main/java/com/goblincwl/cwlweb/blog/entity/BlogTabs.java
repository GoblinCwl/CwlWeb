package com.goblincwl.cwlweb.blog.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 文章标签 Entity
 *
 * @author ☪wl
 * @date 2021-05-09 0:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogTabs implements Serializable {

    private static final long serialVersionUID = -191188447316523043L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 标签名称
     */
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY, condition = SqlCondition.LIKE)
    private String name;

    /**
     * 标签颜色
     */
    private String color;
}
