package com.goblincwl.cwlweb.index.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {
    /**
     * 查询聊天历史记录（时间倒序）
     *
     * @param num 条数
     * @return 结果集合
     * @date 2021-05-03 14:21:53
     * @author ☪wl
     */
    List<ChatMessage> selectListLimit(@Param("num") int num);
}
