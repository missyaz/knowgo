package com.fw.know.go.order.validator;

import com.fw.know.go.api.order.request.OrderCreateRequest;
import com.fw.know.go.order.OrderException;

/**
 * @Description 订单校验，使用模板设计模式进行一个设计
 * 该类是一个抽象类，用于定义订单校验器的模板方法
 * <p>
 *     责任链校验方式：当有下一个校验器时，一直进行校验
 *     具体校验逻辑在doValidate方法中实现，子类需要实现该方法
 *     如果校验失败，抛出OrderException异常
 * </p>
 * @Date 7/4/2026 上午10:00
 * @Author Leo
 */
public class BaseOrderCreateValidator implements OrderCreateValidator {

    protected OrderCreateValidator nexValidator;

    @Override
    public void setNext(OrderCreateValidator next) {
        this.nexValidator = next;
    }

    @Override
    public OrderCreateValidator getNext() {
        return nexValidator;
    }

    @Override
    public void validate(OrderCreateRequest request) throws OrderException {
        doValidate(request);

        if (nexValidator != null) {
            nexValidator.validate(request);
        }
    }

    /**
     * 校验方法的具体实现
     * @param request 创建订单请求
     * @throws OrderException 订单业务异常
     */
    protected abstract void doValidate(OrderCreateRequest request) throws OrderException;
}
