package com.goblincwl.cwlweb.common.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

/**
 * 描述：读取日志信息
 *
 * @author sakyoka
 * @date 2022年8月14日 上午11:01:14
 */
@ServerEndpoint("/log")
@Component
public class ManagerLogWebSocketHandler {

    private Process process;

    private InputStream inputStream;

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    @OnOpen
    public void onOpen(Session session) throws IOException {

        Map<String, List<String>> params = session.getRequestParameterMap();
        String logPath = "logs/goblincw-web.log";
        if (params.containsKey("logPath")) {
            logPath = params.get("logPath").get(0);
        }
        String cmd = "tail -f " + logPath;
        process = Runtime.getRuntime().exec(cmd);
        inputStream = process.getInputStream();

        EXECUTOR_SERVICE.execute(() -> {
            String line;
            BufferedReader reader;
            try {
                reader = new BufferedReader(new InputStreamReader(inputStream));
                while ((line = reader.readLine()) != null) {
                    session.getBasicRemote().sendText(line + "<br>");
                }
            } catch (IOException ignored) {

            }
        });

    }

    @OnMessage
    public void onMessage(String message, Session session) {
//		log.debug(String.format("socket onmessage ==> 接收到信息:%s", message));
    }

    @OnClose
    public void onClose(Session session) {
        this.close();
//    	log.debug(String.format("socket已关闭"));
    }

    @OnError
    public void onError(Throwable thr) {
        this.close();
//    	log.debug(String.format("socket异常，errorMessage:%s" , thr.getMessage()));
    }

    private void close() {

        //这里应该先停止命令, 然后再关闭流
        if (process != null) {
            process.destroy();
        }

        try {
            if (Objects.nonNull(inputStream)) {
                inputStream.close();
            }
        } catch (Exception ignored) {
        }
    }
}