package com.fw.know.go.api.order.request;

import com.fw.know.go.api.goods.constant.GoodsType;
import com.fw.know.go.api.order.constant.TradeOrderEvent;
import com.fw.know.go.api.user.constant.UserType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @Description
 * @Date 2/4/2026 上午10:46
 * @Author Leo
 */
@Getter
@Setter
public class OrderCreateRequest extends BaseOrderRequest{

    /**
     * 买家Id
     */
    @NotNull(message = "买家ID不能为空")
    private String buyerId;

    /**
     * 买家类型
     */
    private UserType buyerType = UserType.CUSTOMER;

    @NotNull(message = "卖家ID不能为空")
    private String sellerId;

    /**
     * 卖家类型
     */
    private UserType sellerType = UserType.PLATFORM;

    /**
     * 订单金额
     */
    @DecimalMin(value = "0.0", inclusive = false, message = "订单金额必须大于0")
    private BigDecimal orderAmount;

    /**
     * 商品ID
     */
    @NotNull(message = "商品ID不能为空")
    private String goodsId;

    /**
     * 商品类型
     */
    private GoodsType goodsType;

    /**
     * 商品图片
     */
    private String goodsPicUrl;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品数量
     */
    @Min(value = 1)
    private int itemCount;

    /**
     * 商品单价
     */
    @DecimalMin(value = "0.0", inclusive = false, message = "商品单价必须大于0")
    private BigDecimal itemPrice;

    /**
     * 快照版本
     */
    private Integer sanpshotVersion;

    /**
     * 交易订单号
     */
    private String orderId;

    @Override
    public TradeOrderEvent getOrderEvent() {
        return TradeOrderEvent.CREATE;
    }
}
