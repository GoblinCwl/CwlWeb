package com.goblincwl.cwlweb.index.mapper;

import com.goblincwl.cwlweb.index.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 聊天消息 Mapper
 *
 * @author ☪wl
 * @date 2021-04-25 14:45
 */
@Mapper
public interface ChatMessageMapper {
    List<ChatMessage> selectListLimit(@Param("num") int num);

    Integer insertOne(ChatMessage chatMessage);
}