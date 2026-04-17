package com.fw.know.go.tcc.configuration;

import com.fw.know.go.tcc.service.TransactionLogService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 事务配置
 * @Date 16/4/2026 下午3:56
 * @Author Leo
 */
@Configuration
@MapperScan("com.fw.know.go.tcc.mapper")
public class TccConfiguration {

    @Bean
    public TransactionLogService transactionLogService(){
        return new TransactionLogService();
    }
}
