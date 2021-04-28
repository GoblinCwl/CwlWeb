package com.goblincwl.cwlweb.common.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * WebSocket配置
 *
 * @author ☪wl
 * @date 2020-12-20 20:00
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer, HandshakeInterceptor {

    private final String endPoint = "websocket";

    /**
     * 注册处理器，拦截器
     *
     * @date 2021-04-28 17:34:15
     * @author ☪wl
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHandler(), endPoint)
                .addInterceptors(new WebSocketConfig())
                .setAllowedOrigins("*");
    }

    /**
     * 握手前
     *
     * @param serverHttpRequest  request
     * @param serverHttpResponse response
     * @param webSocketHandler   handler
     * @param map                map
     * @return 是否通过
     * @date 2021-04-28 17:34:55
     * @author ☪wl
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, org.springframework.web.socket.WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        //验证请求的Origin
        HttpServletRequest request = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
        String requestUrl = request.getRequestURL().toString().replaceAll(endPoint, "");
        System.out.println("requestUrl: " + requestUrl);
        String origin = request.getHeader("Origin");
        System.out.println("origin: " + origin);
        return (origin + "/").equals(requestUrl);
    }

    /**
     * 握手后
     *
     * @param serverHttpRequest  request
     * @param serverHttpResponse response
     * @param webSocketHandler   handler
     * @param e                  e
     * @date 2021-04-28 17:35:12
     * @author ☪wl
     */
    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, org.springframework.web.socket.WebSocketHandler webSocketHandler, Exception e) {
    }
}