package com.fw.know.go.user;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author Leo
 * @Date 2025/11/3 20:44
 * @Description
 */
@EnableDubbo
@SpringBootApplication(scanBasePackages = {"com.fw.know.go.user"})
public class KnowGoUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnowGoUserApplication.class, args);
    }
}
