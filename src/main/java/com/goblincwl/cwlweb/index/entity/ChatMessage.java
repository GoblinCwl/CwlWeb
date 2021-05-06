package com.goblincwl.cwlweb.index.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 聊天消息 Entity
 *
 * @author ☪wl
 * @date 2021-04-25 14:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage implements Serializable {

    private static final long serialVersionUID = 4927482340963579808L;
    /**
     * 主键
     */
    private Integer id;
    /**
     * 消息内容
     */
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY,condition = SqlCondition.LIKE)
    private String content;
    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 渲染时为其添加颜色
     */
    @TableField(exist = false)
    private String color;

    public ChatMessage(String content) {
        this.content = content;
        this.sendTime = new Date();
    }

    public ChatMessage(String content, String color) {
        this.content = content;
        this.sendTime = new Date();
        this.color = color;
    }

    public String toJson() {
        return JSONObject.toJSONStringWithDateFormat(this, "HH:mm:ss");
    }
}
