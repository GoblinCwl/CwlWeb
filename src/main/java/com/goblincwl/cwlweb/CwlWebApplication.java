package com.goblincwl.cwlweb;

import com.goblincwl.cwlweb.common.utils.BeanUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@EnableCaching
@SpringBootApplication
public class CwlWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(CwlWebApplication.class, args);
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
