package com.fw.know.go.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description
 * @Date 21/1/2026 下午1:41
 * @Author Leo
 */
@SpringBootApplication(scanBasePackages = {"com.fw.know.go.auth"})
public class KnowGoAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnowGoAuthApplication.class, args);
    }
}
