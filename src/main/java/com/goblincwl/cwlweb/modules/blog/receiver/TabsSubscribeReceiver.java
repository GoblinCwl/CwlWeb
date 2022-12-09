package com.goblincwl.cwlweb.modules.blog.receiver;

import com.goblincwl.cwlweb.common.utils.EmailUtil;
import com.goblincwl.cwlweb.modules.blog.entity.BlogTabsSubscribe;
import com.goblincwl.cwlweb.modules.blog.entity.Comment;
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
@RabbitListener(queues = "tabsSubscribeQueue")
public class TabsSubscribeReceiver {

    @RabbitHandler
    public void process(Map<String, Object> dataMap) throws Exception {
        String blogTitle = (String) dataMap.get("blogTitle");
        BlogTabsSubscribe subscribe = (BlogTabsSubscribe) dataMap.get("subscribe");
        String email = subscribe.getEmail();
        EmailUtil.sendMail(email, email, "Cwl-Web 订阅内容更新", "您订阅的" + subscribe.getBlogTabsName() + "标签更新了文章：" + blogTitle);
    }
}