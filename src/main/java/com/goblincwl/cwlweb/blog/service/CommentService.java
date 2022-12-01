package com.goblincwl.cwlweb.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goblincwl.cwlweb.blog.entity.Comment;
import com.goblincwl.cwlweb.blog.mapper.CommentMapper;
import com.goblincwl.cwlweb.common.entity.GoblinCwlException;
import com.goblincwl.cwlweb.common.utils.BadWordUtil;
import com.goblincwl.cwlweb.manager.entity.AccessRecord;
import com.goblincwl.cwlweb.manager.service.AccessRecordService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 评论 Service
 *
 * @author ☪wl
 * @date 2021-05-10 15:37
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class CommentService extends ServiceImpl<CommentMapper, Comment> {

    @Resource(name = "redisStringTemplate")
    private RedisTemplate<String, Object> redisTemplate;
    private final RabbitTemplate rabbitTemplate;
    private final AccessRecordService accessRecordService;

    public Integer add(Comment comment) throws IOException {
        //检查用户昵称违禁词
        String nickName = comment.getNickName();
        if (BadWordUtil.isContaintBadWord(nickName, 1)) {
            throw new GoblinCwlException("昵称不合法，请修改后重试！");
        }

        //替换评论内容违禁词
        String contentSafe = BadWordUtil.replaceBadWord(comment.getContent(), 2, "*");
        comment.setContent(contentSafe);
        //如果网址为空，则直接审核通过
        comment.setWebsiteAudit(StringUtils.isEmpty(comment.getWebsite()) ? 1 : 0);

        //保存评论
        this.baseMapper.insert(comment);
        //更新用户信息
        AccessRecord accessRecord = this.accessRecordService.getOne(
                new LambdaQueryWrapper<AccessRecord>()
                        .eq(AccessRecord::getIpAddress, comment.getIpAddress())
        );
        if (accessRecord != null) {
            accessRecord.setNickName(nickName);
            //更新访问记录
            this.accessRecordService.updateById(accessRecord);
            //更新Redis缓存
            redisTemplate.opsForValue().set("ipAccessCache:" + accessRecord.getIpAddress(), accessRecord);
            //设置有效期为30min
            redisTemplate.expire("ipAccessCache:" + accessRecord.getIpAddress(), 30, TimeUnit.MINUTES);
        }

        //为子评论，判断是否需要发送邮件
        if (comment.getParentId() != null && comment.getForId() != null) {
            //获取父评论
            Comment parentComment = this.baseMapper.selectById(comment.getForId());
            //父评论不是自己，父评论邮箱不为空
            if (parentComment != null && !comment.getEmail().equals(parentComment.getEmail()) && StringUtils.isNotEmpty(parentComment.getEmail())) {
                Map<String, Comment> commentMap = new HashMap<>(2);
                commentMap.put("comment", comment);
                commentMap.put("parentComment", parentComment);
                //存入消息队列，待发送邮件
                rabbitTemplate.convertAndSend("defaultExchange", "commentReplyEmailRoute", commentMap);
            }
        }

        return comment.getId();
    }
}
