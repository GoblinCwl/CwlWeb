package com.goblincwl.cwlweb.common.config;
 
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import javax.servlet.http.HttpSession;
import java.util.Map;
 
/**
 * 注意这里没有加 @Component，否则会报错，可能是加载顺序的关系，没有详细研究
 * 调用的时候，使用 new DiyWoShouLanJieQi() 的方式
 */
public class DiyWoShouLanJieQi implements HandshakeInterceptor {
 
    /**
     * 在握手之前执行该方法, 继续握手返回true, 中断握手返回false
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
 
        if(serverHttpRequest instanceof ServletServerHttpRequest){
            ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) serverHttpRequest;
            //获取sessionId传递
            HttpSession session =  serverRequest.getServletRequest().getSession();
            String sessionId = session.getId();
            map.put("sessionId",sessionId);
            System.out.println("握手拦截器: beforeHandshake:"+sessionId);
        }
 
        return true;
    }
 
    /**
     * 在握手之后执行该方法
     */
    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
 
        if(serverHttpRequest instanceof ServletServerHttpRequest){
            ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) serverHttpRequest;
            //获取sessionId传递
            HttpSession session =  serverRequest.getServletRequest().getSession();
            String sessionId = session.getId();
            System.out.println("握手拦截器: afterHandshake:"+sessionId);
        }
    }
}