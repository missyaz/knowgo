package com.fw.know.go.order.configuration;

import com.fw.know.go.api.goods.service.GoodsFacadeService;
import com.fw.know.go.api.user.service.UserFacadeService;
import com.fw.know.go.order.sharding.id.WorkerIdHolder;
import com.fw.know.go.order.validator.GoodsValidator;
import com.fw.know.go.order.validator.OrderCreateValidator;
import com.fw.know.go.order.validator.UserValidator;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @Description
 * @Date 8/4/2026 下午4:52
 * @Author Leo
 */
@Configuration
public class OrderClientConfiguration {

    @Bean
    public WorkerIdHolder workerIdHolder(RedissonClient redissonClient){
        return new WorkerIdHolder(redissonClient);
    }

    @Bean
    @Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
    public GoodsValidator goodsValidator(GoodsFacadeService goodsFacadeService){
        return new GoodsValidator(goodsFacadeService);
    }

    @Bean
    @Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
    public UserValidator userValidator(UserFacadeService userFacadeService){
        return new UserValidator(userFacadeService);
    }

    @Bean
    public OrderCreateValidator orderValidatorChain(UserValidator userValidator, GoodsValidator goodsValidator) {
        userValidator.setNext(goodsValidator);
        return userValidator;
    }
}
