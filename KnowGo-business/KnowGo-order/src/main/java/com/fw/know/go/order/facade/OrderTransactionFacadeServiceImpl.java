package com.fw.know.go.order.facade;

import com.fw.know.go.api.order.OrderTransactionFacadeService;
import com.fw.know.go.api.order.request.OrderConfirmRequest;
import com.fw.know.go.api.order.request.OrderCreateRequest;
import com.fw.know.go.api.order.response.OrderResponse;
import com.fw.know.go.rpc.facade.Facade;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description
 * @Date 8/4/2026 下午2:52
 * @Author Leo
 */
@DubboService(version = "1.0.0")
@RequiredArgsConstructor
public class OrderTransactionFacadeServiceImpl implements OrderTransactionFacadeService {

    @Override
    @Facade
    @Transactional(rollbackFor = Exception.class)
    public OrderResponse tryOrder(OrderCreateRequest orderCreateRequest, String businessScene) {
        // TODO： 记录事务日志
        return new OrderResponse.OrderResponseBuilder().buildSuccess();
    }

    @Override
    @Facade
    @Transactional(rollbackFor = Exception.class)
    public OrderResponse confirmOrder(OrderConfirmRequest orderConfirmRequest, String businessScene) {
        // TODO： 记录事务日志
        return new OrderResponse.OrderResponseBuilder().buildSuccess();
    }
}
