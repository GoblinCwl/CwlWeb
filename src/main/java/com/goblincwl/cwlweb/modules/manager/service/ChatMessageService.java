package com.goblincwl.cwlweb.modules.manager.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goblincwl.cwlweb.modules.manager.entity.ChatMessage;
import com.goblincwl.cwlweb.modules.manager.mapper.ChatMessageMapper;
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
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class ChatMessageService extends ServiceImpl<ChatMessageMapper, ChatMessage> {

    private final ChatMessageMapper chatMessageMapper;

    /**
     * 查询消息历史记录
     *
     * @param num 要查询的 消息历史记录 条数
     * @return 消息历史记录集合
     * @date 2021-04-27 23:01:05
     * @author ☪wl
     */
    public List<ChatMessage> findHistoryList(int num) {
        return this.chatMessageMapper.selectListLimit(num);
    }
}
