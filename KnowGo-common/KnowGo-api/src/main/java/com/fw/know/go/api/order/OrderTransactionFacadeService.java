package com.fw.know.go.api.order;

import com.fw.know.go.api.order.request.OrderConfirmRequest;
import com.fw.know.go.api.order.request.OrderCreateRequest;
import com.fw.know.go.api.order.response.OrderResponse;

/**
 * @Description 订单事务服务
 * @Date 7/4/2026 下午2:49
 * @Author Leo
 */
public interface OrderTransactionFacadeService {

    /**
     * 创建订单
     * @param orderCreateRequest 订单创建请求
     * @param businessScene 业务场景
     * @return 订单响应
     */
    public OrderResponse tryOrder(OrderCreateRequest orderCreateRequest, String businessScene);

    /**
     * 确认订单
     * @param orderConfirmRequest 订单确认请求
     * @param businessScene 业务场景
     * @return 订单响应
     */
    public OrderResponse confirmOrder(OrderConfirmRequest orderConfirmRequest, String businessScene);
}
