package com.goblincwl.cwlweb.common.config;

import com.goblincwl.cwlweb.common.interceptor.WebSocketInterceptor;
import com.goblincwl.cwlweb.common.handler.IndexTerminalWebSocketHandler;
import com.goblincwl.cwlweb.modules.manager.service.KeyValueOptionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket配置
 *
 * @author ☪wl
 * @date 2020-12-20 20:00
 */

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final KeyValueOptionsService keyValueOptionsService;

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * 注册处理器，拦截器
     *
     * @date 2021-04-28 17:34:15
     * @author ☪wl
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new IndexTerminalWebSocketHandler(), "/terminal")
                .addInterceptors(new WebSocketInterceptor(keyValueOptionsService))
                .setAllowedOrigins("*");
    }
}