package com.goblincwl.cwlweb.common.bean;

import com.goblincwl.cwlweb.common.utils.BeanUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 工具 Bean 集中配置
 *
 * @author ☪wl
 * @date 2021-05-03 13:44
 */
@Configuration
public class UtilBean {

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
