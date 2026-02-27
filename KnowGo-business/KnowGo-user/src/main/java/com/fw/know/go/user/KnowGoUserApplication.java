package com.fw.know.go.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author Leo
 * @Date 2025/11/3 20:44
 * @Description
 */
@SpringBootApplication(scanBasePackages = {"com.fw.know.go.user"})
public class KnowGoUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnowGoUserApplication.class, args);
    }
}
