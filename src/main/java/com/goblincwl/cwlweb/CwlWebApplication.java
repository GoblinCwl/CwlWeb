package com.goblincwl.cwlweb;

import com.goblincwl.cwlweb.common.utils.BeanUtil;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 项目启动类
 *
 * @author GoblinCwl
 */
@Controller
@EnableCaching
@SpringBootApplication
public class CwlWebApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(CwlWebApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }

    /**
     * 首页
     */
    @GetMapping(value = "/")
    public String index() {
        return "index";
    }

    /**
     * 页面重定向
     *
     * @param request 请求对象
     * @return 视图
     * @date 2021-04-28 00:48:01
     * @author ☪wl
     */
    @GetMapping(value = "/redirect/**")
    public String redirect(HttpServletRequest request) {
        String prefix = "/redirect/";
        return request.getRequestURI().substring(prefix.length());
    }

    /**
     * 注入BeanUtil，用来特殊情况下获取bean
     *
     * @return BeanUtil
     */
    @Bean("beanUtil")
    public BeanUtil beanUtil() {
        return new BeanUtil();
    }

}
