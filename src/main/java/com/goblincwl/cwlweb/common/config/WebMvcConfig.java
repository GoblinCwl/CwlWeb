package com.goblincwl.cwlweb.common.config;

import com.goblincwl.cwlweb.common.interceptor.WebMvcInterceptor;
import com.goblincwl.cwlweb.manager.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web拦截器
 * 用于判断请求为重定向和api
 *
 * @author ☪wl
 * @date 2021-04-28 00:54:10
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final TokenService tokenService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new WebMvcInterceptor(this.tokenService))
                .addPathPatterns("/manager/**")
                .excludePathPatterns("/",
                        //静态资源
                        "/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg",
                        "/**/*.jpeg", "/**/*.gif", "/**/fonts/**", "/**/*.svg",
                        "/**/*.ico",
                        //错误
                        "/error",
                        //页面重定向
                        "/redirect/**",
                        //管理员登陆
                        "/manager/login"
                );
    }

}