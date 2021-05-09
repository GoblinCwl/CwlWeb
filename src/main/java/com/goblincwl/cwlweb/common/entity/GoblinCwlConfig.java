package com.goblincwl.cwlweb.common.entity;

import com.goblincwl.cwlweb.common.other.YamlPropertySourceFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义配置
 *
 * @author ☪wl
 * @date 2021-04-28 21:33
 */
@PropertySource(
        value = "classpath:GoblinCwlConfig.yml",
        encoding = "utf-8",
        factory = YamlPropertySourceFactory.class
)
@Data
@ConfigurationProperties("goblin-cwl-config")
@Component
@AllArgsConstructor
@NoArgsConstructor
public class GoblinCwlConfig {
    /**
     * webSocket访问节点
     */
    private String webSocketEndpoint;

    /**
     * webSocket Origin白名单
     */
    private List<String> webSocketOriginWhiteList;

    /**
     * 阿里云OSS Bucket名称
     */
    private String ossBucket;

    /**
     * 阿里云OSS Bucket域名
     */
    private String ossDomain;

    /**
     * 阿里云OSS 地域节点
     */
    private String ossEndpoint;

    /**
     * 阿里云OSS API密钥ID
     */
    private String ossAccessKeyId;

    /**
     * 阿里云OSS API密钥
     */
    private String ossAccessKeySecret;

}
