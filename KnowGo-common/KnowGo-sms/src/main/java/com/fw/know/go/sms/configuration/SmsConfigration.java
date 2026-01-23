package com.fw.know.go.sms.configuration;

import com.fw.know.go.sms.MockSmsServiceImpl;
import com.fw.know.go.sms.SmsService;
import com.fw.know.go.sms.SmsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @Description 短信服务配置类
 * @Date 23/1/2026 下午2:01
 * @Author Leo
 */
@Configuration
@EnableConfigurationProperties(SmsProperties.class)
public class SmsConfigration {

    @Autowired
    private SmsProperties smsProperties;

    @Bean
    @ConditionalOnMissingBean
    @Profile({"default", "prod"})
    public SmsService smsService(){
        // 默认或者正式环境，使用真实的配置
        SmsService smsService = new SmsServiceImpl();
        return smsService;
    }

    @Bean
    @ConditionalOnMissingBean
    @Profile({"dev", "test"})
    public SmsService mockSmsService(){
        // 默认或者正式环境，使用模拟的配置
        return new MockSmsServiceImpl();
    }
}
