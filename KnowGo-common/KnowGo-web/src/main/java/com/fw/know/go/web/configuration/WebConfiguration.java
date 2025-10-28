package com.fw.know.go.web.configuration;

import com.fw.know.go.web.handler.GlobalWebExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

/**
 * @Classname WebConfiguration
 * @Description 配置类，用于配置Web相关的Bean
 * @Date 28/10/2025 上午11:19
 * @Author Leo
 */
@AutoConfiguration
@ConditionalOnWebApplication
public class WebConfiguration {

    @Bean
    @ConditionalOnMissingBean(GlobalWebExceptionHandler.class)
    public GlobalWebExceptionHandler globalWebExceptionHandler() {
        return new GlobalWebExceptionHandler();
    }
}
