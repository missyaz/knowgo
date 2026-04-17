package com.fw.know.go.api.order.constant;

import com.fw.know.go.base.exception.ErrorCode;
import lombok.Getter;

/**
 * @Description
 * @Date 7/4/2026 上午10:13
 * @Author Leo
 */
@Getter
public enum OrderErrorCode implements ErrorCode {
    /**
     * 商品不可用
     */
    GOODS_NOT_AVAILABLE("GOODS_NOT_AVAILABLE", "商品不可用"),

    /**
     * 商品价格发生变化
     */
    GOODS_PRICE_CHANGED("GOODS_PRICE_CHANGED", "商品价格发生变化"),

    /**
     * 买家不能是平台用户
     */
    BUYER_IS_PLATFORM_USER("BUYER_IS_PLATFORM_USER", "买家不能是平台用户"),

    /**
     * 买家状态异常
     */
    BUYER_STATUS_ABNORMAL("BUYER_STATUS_ABNORMAL", "买家状态异常"),

    /**
     * 买家未完成实名认证
     */
    BUYER_NOT_AUTH("BUYER_NOT_AUTH", "买家未完成实名认证"),

    /**
     * 创建订单失败
     */
    CREATE_ORDER_FAILED("CREATE_ORDER_FAILED", "创建订单失败"),
    ;

    private final String code;

    private final String message;

    OrderErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
