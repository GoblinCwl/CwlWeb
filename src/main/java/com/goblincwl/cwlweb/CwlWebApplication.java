package com.goblincwl.cwlweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class CwlWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(CwlWebApplication.class, args);
    }

}
