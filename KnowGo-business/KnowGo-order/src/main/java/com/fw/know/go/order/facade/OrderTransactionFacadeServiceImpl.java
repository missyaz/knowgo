package com.fw.know.go.order.facade;

import cn.hutool.core.lang.Assert;
import com.fw.know.go.api.order.OrderTransactionFacadeService;
import com.fw.know.go.api.order.constant.OrderErrorCode;
import com.fw.know.go.api.order.request.OrderConfirmRequest;
import com.fw.know.go.api.order.request.OrderCreateRequest;
import com.fw.know.go.api.order.response.OrderResponse;
import com.fw.know.go.base.exception.BizException;
import com.fw.know.go.order.domain.service.OrderManageService;
import com.fw.know.go.rpc.facade.Facade;
import com.fw.know.go.tcc.entity.TransTrySuccessType;
import com.fw.know.go.tcc.entity.TransactionLog;
import com.fw.know.go.tcc.request.TccRequest;
import com.fw.know.go.tcc.response.TransactionTryResponse;
import com.fw.know.go.tcc.service.TransactionLogService;
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

    private final TransactionLogService transactionLogService;

    private final OrderManageService orderManageService;

    @Override
    @Facade
    @Transactional(rollbackFor = Exception.class)
    public OrderResponse tryOrder(OrderCreateRequest orderCreateRequest, String businessScene) {
        TransactionTryResponse transactionTryResponse = transactionLogService.tryTransaction(new TccRequest(orderCreateRequest.getOrderId(),
                businessScene, "ORDER"));
        Assert.isTrue(transactionTryResponse.getSuccess(), "transaction try failed");

        if (transactionTryResponse.getTransTrySuccessType() == TransTrySuccessType.TRY_SUCCESS){
            // Try成功，创建订单
            OrderResponse orderResponse = orderManageService.create(orderCreateRequest);
            Assert.isTrue(orderResponse.getSuccess(), () -> new BizException(OrderErrorCode.CREATE_ORDER_FAILED));
            return orderResponse;
        }
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
