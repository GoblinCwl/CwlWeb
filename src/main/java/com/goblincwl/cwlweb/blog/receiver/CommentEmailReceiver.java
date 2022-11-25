package com.goblincwl.cwlweb.blog.receiver;

import com.goblincwl.cwlweb.blog.entity.Comment;
import com.goblincwl.cwlweb.common.utils.EmailUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 回复评论 发送队列中的邮件
 *
 * @author ☪wl
 * @date 2022/11/23 22:01
 */
@Component
@RabbitListener(queues = "commentReplyEmailQueue")
public class CommentEmailReceiver {

    private final static String MAIL_TITLE = "GoblinCwl-Blog 评论收到新回复";

    @RabbitHandler
    public void process(Map<String, Comment> commentMap) throws Exception {
        Comment comment = commentMap.get("comment");
        Comment parentComment = commentMap.get("parentComment");

        //构建HTML
        String html = EmailUtil.readHtmlToString("static/other/replyCommentEmailHtmlTemplate.html");
        html = html.replace("${name}", parentComment.getNickName());
        html = html.replace("${imgSrc}", parentComment.getProfileUrl());
        html = html.replace("${replyName}", comment.getNickName());
        html = html.replace("${blogUrl}", "http://localhost:8500/blog/content/" + comment.getBlogId() + "#C" + comment.getId());
        //todo
        html = html.replace("${unsubscribeUrl}", "http://localhost:8500/blog/content/" + comment.getBlogId());

        EmailUtil.sendMail(parentComment.getNickName(), parentComment.getEmail(), MAIL_TITLE, html);
    }
}