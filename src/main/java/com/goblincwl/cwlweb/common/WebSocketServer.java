package com.goblincwl.cwlweb.common;

import com.goblincwl.cwlweb.common.utils.BadWordUtil;
import com.goblincwl.cwlweb.common.utils.BeanUtil;
import com.goblincwl.cwlweb.index.entity.ChatMessage;
import com.goblincwl.cwlweb.index.service.ChatMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * WebSocket消息推送服务
 *
 * @author ☪wl
 * @date 2020-12-20 20:00
 */
@Component
@ServerEndpoint(value = "/websocket")
public class WebSocketServer {

    @PostConstruct
    public void init() {
        LOG.info("Init WebSocket");
    }

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServer.class);

    /**
     * 在线人数
     */
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);

    /**
     * concurrent包的线程安全Set
     * 用来存放每个客户端对应的Session对象
     */
    private static final CopyOnWriteArraySet<Session> SESSION_SET = new CopyOnWriteArraySet<>();

    /**
     * 连接建立成功调用的方法
     *
     * @param session 会话对象
     */
    @OnOpen
    public void onOpen(Session session) {
        SESSION_SET.add(session);
        // 在线数加1
        int cnt = ONLINE_COUNT.incrementAndGet();
        sendMessage(session, new ChatMessage("连接成功，每次将只会加载10条历史记录。", "#2BD92B").toJson());
        ChatMessageService chatMessageService = BeanUtil.getBean(ChatMessageService.class);
        List<ChatMessage> chatMessageList = chatMessageService.findHistoryList(15);
        for (ChatMessage chatMessage : chatMessageList) {
            broadCastInfo(chatMessage.toJson());
        }
        LOG.info("New connections added,current connections: {}", cnt);
    }

    /**
     * 连接关闭调用的方法
     *
     * @param session 会话对象
     */
    @OnClose
    public void onClose(Session session) {
        SESSION_SET.remove(session);
        int cnt = ONLINE_COUNT.decrementAndGet();
        LOG.info("There are connections closed，current connections: {}", cnt);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 会话对象
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        LOG.info("来自客户端的消息：{}", message);
        //敏感词处理
        Set<String> badWords = BadWordUtil.getBadWord(message, 2);
        for (String badWord : badWords) {
            message = message.replaceAll(badWord, "**");
        }
        ChatMessageService chatMessageService = BeanUtil.getBean(ChatMessageService.class);
        ChatMessage chatMessage = new ChatMessage(message);
        chatMessageService.save(chatMessage);
        broadCastInfo(chatMessage.toJson());
    }

    /**
     * 出现错误
     *
     * @param session 会话对象
     * @param error   异常对象
     */
    @OnError
    public void onError(Session session, Throwable error) {

        sendMessage(session, new ChatMessage("发生错误，信息如下，请联系站长！", "red").toJson());
        sendMessage(session, new ChatMessage(error.getMessage(), "red").toJson());
        error.printStackTrace();
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化.
     *
     * @param session 会话
     * @param message 发送的消息
     */
    public static void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            LOG.error("Send Message Error: {}", e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 群发消息
     *
     * @param message 发送的消息
     */
    public static void broadCastInfo(String message) {
        for (Session session : SESSION_SET) {
            if (session.isOpen()) {
                sendMessage(session, message);
            }
        }
    }


    /**
     * 指定Session发送消息
     *
     * @param sessionId 会话ID
     * @param message   发送的消息
     */
    public static void sendMessage(String message, String sessionId) {
        Session session = null;
        for (Session s : SESSION_SET) {
            if (s.getId().equals(sessionId)) {
                session = s;
                break;
            }
        }
        if (session != null) {
            sendMessage(session, message);
        } else {
            LOG.warn("没有找到你指定ID的会话：{}", sessionId);
        }
    }
}