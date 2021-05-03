package com.goblincwl.cwlweb;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 项目启动类
 *
 * @author GoblinCwl
 */
@EnableCaching
@SpringBootApplication
public class CwlWebApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(CwlWebApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }
}
