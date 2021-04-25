package com.goblincwl.cwlweb.index.service;

import com.goblincwl.cwlweb.index.entity.ChatMessage;
import com.goblincwl.cwlweb.index.mapper.ChatMessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 聊天消息 Service
 *
 * @author ☪wl
 * @date 2021-04-25 14:46
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageMapper chatMessageMapper;

    public List<ChatMessage> findHistoryList(int num) {
        return this.chatMessageMapper.selectListLimit(num);
    }

    public Integer saveOne(ChatMessage chatMessage) {
        return this.chatMessageMapper.insertOne(chatMessage);
    }
}
