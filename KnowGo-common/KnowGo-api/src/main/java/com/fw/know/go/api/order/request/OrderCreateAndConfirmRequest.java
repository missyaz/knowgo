package com.fw.know.go.api.order.request;

import com.fw.know.go.api.order.constant.TradeOrderEvent;
import com.fw.know.go.api.user.constant.UserType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Description
 * @Date 2/4/2026 上午10:53
 * @Author Leo
 */
@Getter
@Setter
public class OrderCreateAndConfirmRequest extends OrderCreateRequest{

    /**
     * 操作时间
     */
    @NotNull(message ="operateTime不能为空")
    private Date operateTime;

    /**
     * 操作人
     */
    @NotNull(message = "operator不能为空")
    private String operator;

    /**
     * 操作人类型
     */
    @NotNull(message = "operatorType不能为空")
    private UserType operatorType;

    /**
     * 是否同步扣减库存
     */
    private boolean syncDecreaseInventory = false;

    @Override
    public TradeOrderEvent getOrderEvent() {
        return TradeOrderEvent.CREATE_AND_CONFIRM;
    }
}
