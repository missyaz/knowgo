package com.fw.know.go.trade.infrastructure.config;

import com.fw.know.go.api.goods.service.GoodsFacadeService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Date 3/4/2026 下午1:44
 * @Author Leo
 */
@Configuration
public class TradeDubboConfiguration {

    @DubboReference(version = "1.0.0")
    private GoodsFacadeService goodsFacadeService;
}
