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
@ConfigurationProperties("websocket")
@Component
@AllArgsConstructor
@NoArgsConstructor
public class GoblinCwlConfig {

    private String endPoint;

    private List<String> originWhiteList;

}
