package com.fw.know.go.order.domain.entity;

import com.fw.know.go.api.goods.constant.GoodsType;
import com.fw.know.go.api.order.constant.TradeOrderState;
import com.fw.know.go.api.pay.constant.PayChannel;
import com.fw.know.go.api.user.constant.UserType;
import com.fw.know.go.datasource.domain.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description
 * @Date 1/4/2026 下午5:10
 * @Author Leo
 */
@Getter
@Setter
public class TradeOrder extends BaseEntity {

    /**
     * 默认超时时间，30分钟
     */
    public static final int DEFAULT_TIME_OUT_MINUTES = 30;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 买家ID
     */
    private String buyerId;

    /**
     * 买家 ID 的逆序
     */
    private String reverseBuyerId;

    /**
     * 买家类型
     */
    private UserType buyerType;

    /**
     * 卖家ID
     */
    private String sellerId;

    /**
     * 卖家类型
     */
    private UserType sellerType;

    /**
     * 幂等号
     */
    private String identifier;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 商品数量
     */
    private int itemCount;

    /**
     * 商品单价
     */
    private BigDecimal itemPrice;

    /**
     * 已支付金额
     */
    private BigDecimal paidAmount;

    /**
     * 支付成功时间
     */
    private Date paySucceedTime;

    /**
     * 下单确认时间
     */
    private Date orderConfirmedTime;

    /**
     * 订单完成时间
     */
    private Date orderFinishedTime;

    /**
     * 订单关闭时间
     */
    private Date orderClosedTime;

    /**
     * 商品ID
     */
    private String goodsId;

    /**
     * 商品类型
     */
    private GoodsType goodsType;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品图片
     */
    private String goodsPicUrl;

    /**
     * 支付方式
     */
    private PayChannel payChannel;

    /**
     * 支付流水号
     */
    private String payStreamId;

    /**
     * 订单状态
     */
    private TradeOrderState orderState;

    /**
     * 关单类型
     */
    private String closeType;

    /**
     * 快照版本
     */
    private Integer snapshotVersion;

}
