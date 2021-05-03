package com.goblincwl.cwlweb.common.config;

import com.goblincwl.cwlweb.common.interceptor.WebMvcInterceptor;
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
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new WebMvcInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/",
                        "/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg",
                        "/**/*.jpeg", "/**/*.gif", "/**/fonts/**", "/**/*.svg",
                        "/**/*.ico",
                        "/error", "/redirect/**"
                );
    }

//    @Bean("error")
//    public View defaultErrorView() {
//        return new TemplateNotFoundView();
//    }


}