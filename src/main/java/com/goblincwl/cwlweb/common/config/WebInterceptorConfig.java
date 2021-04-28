package com.goblincwl.cwlweb.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * web拦截器
 * 用于判断请求为重定向和api
 *
 * @author ☪wl
 * @date 2021-04-28 00:54:10
 */
@Configuration
public class WebInterceptorConfig implements HandlerInterceptor, WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new WebInterceptorConfig())
                .addPathPatterns("/**")
                .excludePathPatterns("/",
                        "/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg",
                        "/**/*.jpeg", "/**/*.gif", "/**/fonts/**", "/**/*.svg",
                        "/**/*.ico",
                        "/error", "/redirect/**"
                );
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String goblinCwlRequestType = request.getHeader("GoblinCwlRequestType");
        if (!"api".equals(goblinCwlRequestType)) {
            response.sendRedirect(request.getContextPath() + "/redirect" + request.getRequestURI());
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }

}