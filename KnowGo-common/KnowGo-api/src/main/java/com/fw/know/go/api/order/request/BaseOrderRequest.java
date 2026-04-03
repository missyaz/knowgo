package com.fw.know.go.api.order.request;

import com.fw.know.go.api.order.constant.TradeOrderEvent;
import com.fw.know.go.base.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description
 * @Date 2/4/2026 上午10:41
 * @Author Leo
 */
@Getter
@Setter
public abstract class BaseOrderRequest extends BaseRequest {

    /**
     * 操作幂等号
     */
    @NotNull(message = "identifier is not null")
    private String identifier;

    /**
     * 获取订单事件
     * @return 订单事件
     */
    public abstract TradeOrderEvent getOrderEvent();
}
