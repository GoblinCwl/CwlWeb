package com.goblincwl.cwlweb.blog.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 博客（文章）Entity
 *
 * @author ☪wl
 * @date 2021-05-07 17:58
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog implements Serializable {

    private static final long serialVersionUID = 5931944009459656289L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 文章标题
     */
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY, condition = SqlCondition.LIKE)
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 发布时间
     */
    @TableField(whereStrategy = FieldStrategy.NOT_NULL)
    private Date releaseTime;

    /**
     * 更新时间
     */
    @TableField(whereStrategy = FieldStrategy.NOT_NULL)
    private Date updateTime;

    /**
     * 文章标签
     */
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String tabs;

    /**
     * 预览内容
     */
    private String shortContent;
}
