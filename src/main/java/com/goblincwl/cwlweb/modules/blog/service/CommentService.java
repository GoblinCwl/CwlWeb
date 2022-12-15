package com.goblincwl.cwlweb.modules.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.goblincwl.cwlweb.common.utils.EmailUtil;
import com.goblincwl.cwlweb.modules.blog.entity.Comment;
import com.goblincwl.cwlweb.modules.blog.mapper.CommentMapper;
import com.goblincwl.cwlweb.common.entity.GoblinCwlException;
import com.goblincwl.cwlweb.common.utils.BadWordUtil;
import com.goblincwl.cwlweb.modules.manager.entity.AccessRecord;
import com.goblincwl.cwlweb.modules.manager.entity.KeyValueOptions;
import com.goblincwl.cwlweb.modules.manager.service.AccessRecordService;
import com.goblincwl.cwlweb.modules.manager.service.KeyValueOptionsService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
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
    private final KeyValueOptionsService keyValueOptionsService;

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
        if (StringUtils.isEmpty(comment.getWebsite())) {
            comment.setWebsiteAudit(1);
        } else {
            //查询白名单
            KeyValueOptions commentWebsiteAuditWhitelist = this.keyValueOptionsService.getById("commentWebsiteAuditWhitelist");
            if (commentWebsiteAuditWhitelist != null) {
                String whiteList = commentWebsiteAuditWhitelist.getOptValue();
                if (whiteList.contains(comment.getWebsite())) {
                    //白名单内有，直接审核通过
                    comment.setWebsiteAudit(1);
                }
            }
        }

        //如果留有邮箱，生成UUID验证码，用于取消订阅操作
        if (StringUtils.isNotEmpty(comment.getEmail())) {
            comment.setVerificationCode(EmailUtil.creatCode(10));
        }

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
            //父评论未取消订阅
            String unsubscribeValue = "Unsubscribe";
            if (parentComment != null && !unsubscribeValue.equals(parentComment.getVerificationCode())) {
                //父评论不是自己，父评论邮箱不为空
                if (!comment.getEmail().equals(parentComment.getEmail()) && StringUtils.isNotEmpty(parentComment.getEmail())) {
                    Map<String, Comment> commentMap = new HashMap<>(2);
                    commentMap.put("comment", comment);
                    commentMap.put("parentComment", parentComment);
                    //存入消息队列，待发送邮件
                    rabbitTemplate.convertAndSend("defaultExchange", "commentReplyEmailRoute", commentMap);
                }
            }
        }

        return comment.getId();
    }

    /**
     * 根据博客ID查询评论ID
     *
     * @param blogIdList 博客ID集合
     * @return 评论ID集合
     * @date 2022/12/8 14:00
     * @author ☪wl
     */
    public List<Integer> idListByBlogIds(List<String> blogIdList) {
        return this.baseMapper.selectIdListByBlogIds(blogIdList);
    }

    /**
     * 网址审核
     *
     * @param id       ID
     * @param auditFlg 审核标识
     * @date 2022/12/8 15:05
     * @author ☪wl
     */
    public boolean doWebsiteAudit(Integer id, Integer auditFlg) {
        //如果是审核通过，将网址添加到白名单
        Comment comment = this.getById(id);
        KeyValueOptions commentWebsiteAuditWhitelist = this.keyValueOptionsService.getById("commentWebsiteAuditWhitelist");
        String website = comment.getWebsite();
        if (commentWebsiteAuditWhitelist != null) {
            String whiteList = commentWebsiteAuditWhitelist.getOptValue();
            boolean whiteListContainsNowWebsite = whiteList.contains(website);
            if (auditFlg == 1) {
                if (!whiteListContainsNowWebsite) {
                    whiteList += "," + website;
                }
            } else {
                //如果不通过，将网址从白名单移除
                if (whiteListContainsNowWebsite) {
                    whiteList = whiteList.replace(website, "").replace(",,", ",");
                }
            }
            commentWebsiteAuditWhitelist.setOptValue(whiteList);
            this.keyValueOptionsService.updateById(commentWebsiteAuditWhitelist);
        }
        //更新评论
        this.update(
                new UpdateWrapper<Comment>()
                        .lambda().eq(Comment::getId, id)
                        .set(id != null, Comment::getWebsiteAudit, auditFlg)
        );
        return true;
    }

    /**
     * 白名单审核
     *
     * @date 2022/12/8 15:17
     * @author ☪wl
     */
    public void whiteListReview() {
        KeyValueOptions commentWebsiteAuditWhitelist = this.keyValueOptionsService.getById("commentWebsiteAuditWhitelist");
        if (commentWebsiteAuditWhitelist != null) {
            String whiteList = commentWebsiteAuditWhitelist.getOptValue();
            if (StringUtils.isNotEmpty(whiteList)) {
                List<String> whiteListList = Arrays.asList(whiteList.split(","));
                this.update(
                        new UpdateWrapper<Comment>().lambda()
                                .in(Comment::getWebsite, whiteListList)
                                .set(true, Comment::getWebsiteAudit, 1)
                );
            }
        }
    }
}
