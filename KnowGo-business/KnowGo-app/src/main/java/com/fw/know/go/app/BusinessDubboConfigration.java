package com.fw.know.go.app;

import com.fw.know.go.api.notice.service.NoticeFacadeService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Date 23/1/2026 下午3:45
 * @Author Leo
 */
@Configuration
public class BusinessDubboConfigration {

    @DubboReference(version = "1.0.0")
    private NoticeFacadeService noticeFacadeService;

    @Bean
    @ConditionalOnMissingBean(name = "noticeFacadeService")
    public NoticeFacadeService noticeFacadeService(){
        return noticeFacadeService;
    }
}
