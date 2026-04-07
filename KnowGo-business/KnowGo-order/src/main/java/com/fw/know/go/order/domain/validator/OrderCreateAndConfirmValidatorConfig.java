package com.fw.know.go.order.domain.validator;

import com.fw.know.go.order.validator.GoodsValidator;
import com.fw.know.go.order.validator.OrderCreateValidator;
import com.fw.know.go.order.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 订单创建校验器配置
 * @Date 7/4/2026 上午11:01
 * @Author Leo
 */
@Configuration
@RequiredArgsConstructor
public class OrderCreateAndConfirmValidatorConfig {

    private final GoodsValidator goodsValidator;

    private final UserValidator userValidator;

    @Bean
    public OrderCreateValidator createValidator(){
        userValidator.setNext(goodsValidator);
        return userValidator;
    }
}
