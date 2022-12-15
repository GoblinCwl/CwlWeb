package com.goblincwl.cwlweb.modules.blog.receiver;

import com.goblincwl.cwlweb.modules.blog.entity.Comment;
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
        //收件人昵称
        html = html.replace("${name}", parentComment.getNickName());
        //收件人头像
        html = html.replace("${imgSrc}", parentComment.getProfileUrl());
        //新评论昵称
        html = html.replace("${replyName}", comment.getNickName());
        //新评论跳转地址
        html = html.replace("${blogUrl}", "http://localhost:8500/blog/content/" + comment.getBlogId() + "#C" + comment.getId());
        //取消订阅地址
        html = html.replace("${unsubscribeUrl}", "http://localhost:8500/blog/comment/unsubscribe/" + parentComment.getId() + "?code=" + parentComment.getVerificationCode());
        //被回复评论地址
        html = html.replace("${commentLink}", "http://localhost:8500/blog/content/" + comment.getBlogId() + "#C" + parentComment.getId());
        //被回复评论内容
        html = html.replace("${commentContent}", parentComment.getContent());
        //新评论地址
        html = html.replace("${newCommentLink}", "http://localhost:8500/blog/content/" + comment.getBlogId() + "#C" + comment.getId());
        //新评论内容
        html = html.replace("${newCommentContent}", comment.getContent());
        //项目地址
        html = html.replace("${projectUrl}","http://localhost:8500");

        EmailUtil.sendMail(parentComment.getNickName(), parentComment.getEmail(), MAIL_TITLE, html);
    }
}