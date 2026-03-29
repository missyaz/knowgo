package com.fw.know.go.goods;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author Leo
 * @Date 2026/3/28 15:59
 * @Description
 */
@SpringBootApplication(scanBasePackages = {"com.fw.know.go.goods","com.fw.know.go.box","com.fw.know.go.collection"})
@EnableDubbo(scanBasePackages = {"com.fw.know.go.goods","com.fw.know.go.box","com.fw.know.go.collection"})
public class KnowGoGoodsApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnowGoGoodsApplication.class, args);
    }
}
