package com.goblincwl.cwlweb.common.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * WebMVC 拦截器
 *
 * @author ☪wl
 * @date 2021-04-28 22:07
 */
public class WebMvcInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String goblinCwlRequestType = request.getHeader("GoblinCwlRequestType");
        if (!"api".equals(goblinCwlRequestType)) {
            request.getRequestDispatcher(request.getContextPath() + "/redirect" + request.getRequestURI()).forward(request, response);
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }

}
