package com.goblincwl.cwlweb.common.config;

import com.goblincwl.cwlweb.common.entity.GoblinCwlConfig;
import com.goblincwl.cwlweb.common.interceptor.WebSocketInterceptor;
import com.goblincwl.cwlweb.common.handler.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

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

    private final GoblinCwlConfig goblinCwlConfig;

    /**
     * 注册处理器，拦截器
     *
     * @date 2021-04-28 17:34:15
     * @author ☪wl
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHandler(), this.goblinCwlConfig.getEndPoint())
                .addInterceptors(new WebSocketInterceptor(goblinCwlConfig))
                .setAllowedOrigins("*");
    }
}