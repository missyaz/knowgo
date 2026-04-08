package com.fw.know.go.trade.infrastructure.config;

import com.fw.know.go.api.goods.service.GoodsFacadeService;
import com.fw.know.go.api.goods.service.GoodsTransactionFacadeService;
import com.fw.know.go.api.order.OrderFacadeService;
import com.fw.know.go.api.order.OrderTransactionFacadeService;
import com.fw.know.go.api.user.service.UserFacadeService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Date 3/4/2026 下午1:44
 * @Author Leo
 */
@Configuration
public class TradeDubboConfiguration {

    @DubboReference(version = "1.0.0")
    private OrderFacadeService orderFacadeService;

    @DubboReference(version = "1.0.0")
    private GoodsFacadeService goodsFacadeService;

    @DubboReference(version = "1.0.0")
    private UserFacadeService userFacadeService;

    @DubboReference(version = "1.0.0")
    private OrderTransactionFacadeService orderTransactionFacadeService;

    @DubboReference(version = "1.0.0")
    private GoodsTransactionFacadeService goodsTransactionFacadeService;

    @Bean
    @ConditionalOnMissingBean(name = "orderFacadeService")
    public OrderFacadeService orderFacadeService() {
        return orderFacadeService;
    }

    @Bean
    @ConditionalOnMissingBean(name = "goodsFacadeService")
    public GoodsFacadeService goodsFacadeService() {
        return goodsFacadeService;
    }

    @Bean
    @ConditionalOnMissingBean(name = "userFacadeService")
    public UserFacadeService userFacadeService() {
        return userFacadeService;
    }

    @Bean
    @ConditionalOnMissingBean(name = "orderTransactionFacadeService")
    public OrderTransactionFacadeService orderTransactionFacadeService() {
        return orderTransactionFacadeService;
    }

    @Bean
    @ConditionalOnMissingBean(name = "goodsTransactionFacadeService")
    public GoodsTransactionFacadeService goodsTransactionFacadeService() {
        return goodsTransactionFacadeService;
    }
}
