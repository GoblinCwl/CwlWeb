package com.goblincwl.cwlweb.index.entity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Integer id;
    private String content;
    private Date sendTime;
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
        return JSONObject.toJSONStringWithDateFormat(this, "HH:mm:ss").toString();
    }
}
