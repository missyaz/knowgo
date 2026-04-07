package com.fw.know.go.order.validator;

import com.fw.know.go.api.order.request.OrderCreateRequest;
import com.fw.know.go.order.OrderException;

/**
 * @Description 订单校验
 * @Date 7/4/2026 上午9:58
 * @Author Leo
 */
public interface OrderCreateValidator {

    /**
     * 设置下一个校验器
     * @param next 下一个校验器
     */
    public void setNext(OrderCreateValidator next);

    /**
     * 获得下一个校验器
     * @return 下一个校验器
     */
    public OrderCreateValidator getNext();

    /**
     * 校验方法
     * @param request 创建订单请求
     * @throws OrderException 订单业务异常
     */
    public void validate(OrderCreateRequest request) throws OrderException;
}
