package com.fw.know.go.app;

import com.fw.know.go.api.box.service.BlindBoxReadFacadeService;
import com.fw.know.go.api.collections.service.CollectionReadFacadeService;
import com.fw.know.go.api.goods.service.GoodsFacadeService;
import com.fw.know.go.api.notice.service.NoticeFacadeService;
import com.fw.know.go.api.user.service.UserFacadeService;
import com.fw.know.go.box.domain.service.BlindBoxService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
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

    @DubboReference(version = "1.0.0")
    private UserFacadeService userFacadeService;

    @DubboReference(version = "1.0.0")
    private CollectionReadFacadeService collectionReadFacadeService;

    @DubboReference(version = "1.0.0")
    private BlindBoxReadFacadeService blindBoxReadFacadeService;

    @DubboReference(version = "1.0.0")
    private GoodsFacadeService goodsFacadeService;

    @Bean
    @ConditionalOnMissingBean(name = "noticeFacadeService")
    public NoticeFacadeService noticeFacadeService(){
        return noticeFacadeService;
    }

    @Bean
    @ConditionalOnMissingBean(name = "userFacadeService")
    public UserFacadeService userFacadeService(){
        return userFacadeService;
    }

    @Bean
    @ConditionalOnMissingBean(name = "blindBoxReadFacadeService")
    public BlindBoxReadFacadeService blindBoxReadFacadeService(){
        return blindBoxReadFacadeService;
    }

    @Bean
    @ConditionalOnMissingBean(name = "collectionReadFacadeService")
    public CollectionReadFacadeService collectionReadFacadeService(){
        return collectionReadFacadeService;
    }

    @Bean
    @ConditionalOnMissingBean(name = "goodsFacadeService")
    public GoodsFacadeService goodsFacadeService(){
        return goodsFacadeService;
    }
}
