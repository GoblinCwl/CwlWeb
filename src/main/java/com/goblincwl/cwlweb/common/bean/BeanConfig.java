package com.goblincwl.cwlweb.common.bean;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.goblincwl.cwlweb.common.utils.BeanUtil;
import com.goblincwl.cwlweb.modules.manager.service.KeyValueOptionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Bean 集中配置
 *
 * @author ☪wl
 * @date 2021-05-03 13:44
 */
@Configuration
@RequiredArgsConstructor
public class BeanConfig {

    /**
     * BeanUtil，用来特殊情况下获取bean
     *
     * @return BeanUtil
     */
    @Bean("beanUtil")
    public BeanUtil beanUtil() {
        return new BeanUtil();
    }

    /**
     * 阿里云OSS实例，单例模式
     *
     * @param keyValueOptionsService 键值对Service
     * @return 阿里云OSS实例
     * @date 2021-05-09 23:52:19
     * @author ☪wl
     */
    @Bean
    public OSS ossBean(KeyValueOptionsService keyValueOptionsService) {
        return new OSSClientBuilder().build(
                keyValueOptionsService.getById("ossEndpoint").getOptValue(),
                keyValueOptionsService.getById("ossAccessKeyId").getOptValue(),
                keyValueOptionsService.getById("ossAccessKeySecret").getOptValue());
    }

}
