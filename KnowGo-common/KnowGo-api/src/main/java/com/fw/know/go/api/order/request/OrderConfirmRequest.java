package com.fw.know.go.api.order.request;

import com.fw.know.go.api.goods.constant.GoodsType;
import com.fw.know.go.api.order.constant.TradeOrderEvent;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description 订单确认请求
 * @Date 7/4/2026 下午3:34
 * @Author Leo
 */
@Getter
@Setter
public class OrderConfirmRequest extends BaseOrderRequest{

    /**
     * 买家Id
     */
    private String buyerId;

    /**
     * 商品Id
     */
    private String goodsId;

    /**
     * 商品类型
     */
    private GoodsType goodsType;

    /**
     * 数量
     */
    private Integer itemCount;

    @Override
    public TradeOrderEvent getOrderEvent() {
        return TradeOrderEvent.CONFIRM;
    }
}
