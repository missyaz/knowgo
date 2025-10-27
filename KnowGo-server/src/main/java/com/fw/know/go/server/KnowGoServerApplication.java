package com.fw.know.go.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Classname KnowGoServerApplication
 * @Description
 * @Date 27/10/2025 上午11:19
 * @Author Leo
 */
@SpringBootApplication(scanBasePackages = {"com.fw.know.go.server"})
public class KnowGoServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(KnowGoServerApplication.class, args);
    }
}
