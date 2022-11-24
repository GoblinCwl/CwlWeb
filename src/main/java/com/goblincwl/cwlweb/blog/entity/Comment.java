package com.goblincwl.cwlweb.blog.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 评论 Entitiy
 *
 * @author ☪wl
 * @date 2021-05-10 15:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment implements Serializable {

    private static final long serialVersionUID = -4559262025213468597L;
    /**
     * 主键
     */
    private Integer id;
    /**
     * 父评论ID
     */
    private Integer parentId;
    /**
     * 回复的评论ID
     */
    private Integer forId;
    /**
     * 所属文章ID
     */
    private Integer blogId;
    /**
     * 所属文章
     */
    @TableField(exist = false)
    private Blog blog;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 头像地址
     */
    private String profileUrl;
    /**
     * 评论内容
     */
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY, condition = SqlCondition.LIKE)
    private String content;
    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 网站
     */
    private String website;

    /**
     * 评论IP
     */
    private String ipAddress;

    /**
     * 网址审核(0.未审核,1.已审核)
     */
    private Integer websiteAudit;

    /**
     * 网址审核(0.未审核,1.已审核) - 多选
     */
    @TableField(exist = false)
    private String websiteAuditStr;

    /**
     * 子评论列表
     */
    @TableField(exist = false)
    private List<Comment> childrenList;

}
