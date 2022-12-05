package com.goblincwl.cwlweb.common.interceptor;

import com.goblincwl.cwlweb.modules.manager.service.KeyValueOptionsService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * WebSocket 拦截器
 *
 * @author ☪wl
 * @date 2021-04-28 22:06
 */
@RequiredArgsConstructor
public class WebSocketInterceptor implements HandshakeInterceptor {

    private final KeyValueOptionsService keyValueOptionsService;

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
    public boolean beforeHandshake(@NotNull ServerHttpRequest serverHttpRequest, @NotNull ServerHttpResponse serverHttpResponse, @NotNull org.springframework.web.socket.WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        //验证请求的Origin
        HttpServletRequest request = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
        String origin = request.getHeader("Origin");
        String webSocketOriginWhiteList = keyValueOptionsService.getById("webSocketOriginWhiteList").getOptValue();
        if (StringUtils.isNotEmpty(webSocketOriginWhiteList)) {
            return webSocketOriginWhiteList.contains(origin);
        }
        return true;
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
    public void afterHandshake(@NotNull ServerHttpRequest serverHttpRequest, @NotNull ServerHttpResponse serverHttpResponse, @NotNull org.springframework.web.socket.WebSocketHandler webSocketHandler, Exception e) {
    }
}
