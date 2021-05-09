package com.goblincwl.cwlweb.common.config;

import com.goblincwl.cwlweb.common.entity.GoblinCwlConfig;
import com.goblincwl.cwlweb.common.interceptor.WebMvcInterceptor;
import com.goblincwl.cwlweb.common.interceptor.WebRedirectInterceptor;
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
    private final GoblinCwlConfig goblinCwlConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //重定向拦截器
        registry.addInterceptor(new WebRedirectInterceptor(this.tokenService))
                .addPathPatterns("/**")
                .excludePathPatterns(
                        //首页
                        "/",
                        //静态资源
                        "/css/**", "/fonts/**", "/images/**", "/js/**", "/plugins/**", "/webjars/**",
                        //错误
                        "/error",
                        //页面重定向
                        "/redirect/**",
                        //manger模块
                        "/manager/**"
                );
        //mvc拦截器
        registry.addInterceptor(new WebMvcInterceptor(this.tokenService, this.goblinCwlConfig))
                .addPathPatterns("/manager/**")
                .excludePathPatterns(
                        //管理员登陆
                        "/manager/login", "/manager/logout"
                );
    }

}