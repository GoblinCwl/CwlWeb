package com.goblincwl.cwlweb.blog.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class Comment {

    /**
     * 主键
     */
    private Integer id;
    /**
     * 父评论ID
     */
    private Integer parentId;
    /**
     * 所属文章ID
     */
    private Integer blogId;
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
     * 子评论列表
     */
    @TableField(exist = false)
    private List<Comment> childrenList;

}
