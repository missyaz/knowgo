package com.fw.know.go.order;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description
 * @Date 1/4/2026 下午5:03
 * @Author Leo
 */
@EnableDubbo
@SpringBootApplication(scanBasePackages = "com.fw.know.go.order")
public class KnowGoOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnowGoOrderApplication.class, args);
    }
}
