package com.goblincwl.cwlweb.modules.blog.receiver;

import com.goblincwl.cwlweb.common.utils.EmailUtil;
import com.goblincwl.cwlweb.modules.blog.entity.Blog;
import com.goblincwl.cwlweb.modules.blog.entity.BlogTabs;
import com.goblincwl.cwlweb.modules.blog.entity.BlogTabsSubscribe;
import com.goblincwl.cwlweb.modules.blog.entity.Comment;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 回复评论 发送队列中的邮件
 *
 * @author ☪wl
 * @date 2022/11/23 22:01
 */
@Component
@RabbitListener(queues = "tabsSubscribeQueue")
public class TabsSubscribeReceiver {

    @RabbitHandler
    public void process(Map<String, Object> dataMap) throws Exception {
        Blog blog = (Blog) dataMap.get("blog");
        BlogTabsSubscribe subscribe = (BlogTabsSubscribe) dataMap.get("subscribe");
        String projectUrl = dataMap.get("serverScheme") + "://" + dataMap.get("serverName") + ":" + dataMap.get("serverPort");

        //构建HTML
        String html = EmailUtil.readHtmlToString("static/other/blogSubscriptionUpdateHtmlTemplate.html");
        //邮箱
        String email = subscribe.getEmail();
        html = html.replace("${email}", email);
        //博客地址
        html = html.replace("${blogUrl}", projectUrl + "/blog/content/" + blog.getId());
        //标题
        html = html.replace("${blogTitle}", blog.getTitle());
        //取消订阅地址
        html = html.replace("${unsubscribeUrl}", projectUrl + "/blog/subscribe");
        //项目地址
        html = html.replace("${projectUrl}", projectUrl);

        boolean isNew = blog.getReleaseTime() == blog.getUpdateTime();
        //前言
        html = html.replace("${preface}", isNew ? "您订阅的标签新发布了一遍博文" : "有一篇包含您订阅标签的博文更新了");
        //图标
        html = html.replace("${headerIcon}", isNew ? "\uD83C\uDF1F" : "\uD83D\uDD14");

        //标签
        StringBuilder blogTabsHtmlSb = new StringBuilder();
        blog.getBlogTabsList().forEach(blogTabs -> blogTabsHtmlSb
                .append("<a class='badge' style='background: ")
                .append(blogTabs.getColor()).append("' href='")
                .append(projectUrl).append("/blog?query=%23")
                .append(blogTabs.getName())
                .append("'>")
                .append(blogTabs.getName())
                .append("</a>"));
        html = html.replace("${blogTabs}", blogTabsHtmlSb.toString());

        String title = "Cwl-Web 订阅内容更新";
        EmailUtil.sendMail(email, email, title, html);
    }
}