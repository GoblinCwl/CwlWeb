package com.goblincwl.cwlweb.common.handler;

import com.goblincwl.cwlweb.common.utils.BadWordUtil;
import com.goblincwl.cwlweb.common.utils.BeanUtil;
import com.goblincwl.cwlweb.manager.entity.ChatMessage;
import com.goblincwl.cwlweb.manager.service.ChatMessageService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 首页终端WebSocket处理器
 *
 * @author ☪wl
 * @date 2020-12-20 20:00
 */
public class IndexTerminalWebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOG = LoggerFactory.getLogger(IndexTerminalWebSocketHandler.class);
    /**
     * 在线人数
     */
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);
    /**
     * concurrent包的线程安全Set
     * 用来存放每个客户端对应的Session对象
     */
    private static final CopyOnWriteArraySet<WebSocketSession> SESSION_SET = new CopyOnWriteArraySet<>();
    /**
     * 消息最大值
     */
    private static final Integer MAX_MESSAGE_LENGTH = 50;

    /**
     * 获取在线人数
     *
     * @return 在线人数
     * @date 2022/12/2 10:29
     * @author ☪wl
     */
    public static Integer getOnlineCount() {
        return ONLINE_COUNT.get();
    }

    /**
     * 连接建立成功调用的方法
     *
     * @param session 会话
     */
    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) {
        SESSION_SET.add(session);
        // 在线数加1
        int cnt = ONLINE_COUNT.incrementAndGet();
        sendMessage(session, new ChatMessage("连接成功，每次将只会加载10条历史记录。👌", "#2BD92B").toJson());
        sendMessage(session, new ChatMessage("当前在线：[" + (ONLINE_COUNT.get()) + "]人。😶‍", "#2BD92B").toJson());
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
     * @param session 会话
     * @param status  状态
     */
    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) {
        SESSION_SET.remove(session);
        int cnt = ONLINE_COUNT.decrementAndGet();
        LOG.info("There are connections closed，current connections: {}", cnt);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param session     会话
     * @param textMessage 消息
     */
    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, @NotNull TextMessage textMessage) {
        try {
            String message = textMessage.getPayload();
            if (message.length() > 50) {
                message = message.substring(0, MAX_MESSAGE_LENGTH + 1) + "...";
            }
            //敏感词处理
            message = BadWordUtil.replaceBadWord(message, 2, "*");
            ChatMessageService chatMessageService = BeanUtil.getBean(ChatMessageService.class);
            ChatMessage chatMessage = new ChatMessage(message);
            chatMessageService.save(chatMessage);
            broadCastInfo(chatMessage.toJson());
        } catch (Exception e) {
            exceptionHandler(session, e);
        }
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化.
     *
     * @param session 会话
     * @param message 发送的消息
     */
    public static void sendMessage(WebSocketSession session, String message) {
        try {
            session.sendMessage(new TextMessage(message));
        } catch (Exception e) {
            exceptionHandler(session, e);
        }
    }

    /**
     * 群发消息
     *
     * @param message 发送的消息
     */
    public static void broadCastInfo(String message) {
        for (WebSocketSession session : SESSION_SET) {
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
        WebSocketSession session = null;
        for (WebSocketSession s : SESSION_SET) {
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

    /**
     * 异常处理
     *
     * @param session   会话
     * @param exception 异常
     * @date 2021-04-28 22:27:50
     * @author ☪wl
     */
    private static void exceptionHandler(WebSocketSession session, Exception exception) {
        LOG.error("WebSocket出现异常: ", exception);
        sendMessage(session, new ChatMessage("发电失败，建议联系站长！⚡", "red").toJson());
    }
}