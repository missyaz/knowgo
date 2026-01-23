package com.fw.know.go.rpc.configuration;

import com.fw.know.go.rpc.facade.FacadeAspect;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description Rpc配置
 * @Date 22/1/2026 上午9:39
 * @Author Leo
 */
@EnableDubbo
@Configuration
public class RpcConfiguration {

    @Bean
    public FacadeAspect facadeAspect(){
        return new FacadeAspect();
    }
}
