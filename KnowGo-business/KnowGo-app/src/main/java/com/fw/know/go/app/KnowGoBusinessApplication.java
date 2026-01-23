package com.fw.know.go.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description
 * @Date 23/1/2026 下午3:39
 * @Author Leo
 */
@SpringBootApplication(scanBasePackages = {"com.fw.know.go"})
public class KnowGoBusinessApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnowGoBusinessApplication.class, args);
    }
}
