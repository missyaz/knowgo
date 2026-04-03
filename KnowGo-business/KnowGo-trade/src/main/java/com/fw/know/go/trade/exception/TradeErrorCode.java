package com.fw.know.go.trade.exception;

import com.fw.know.go.base.exception.ErrorCode;
import lombok.Getter;

/**
 * @Description
 * @Date 2/4/2026 上午10:32
 * @Author Leo
 */
public enum TradeErrorCode implements ErrorCode {

    /**
     * 订单创建失败
     */
    ORDER_CREATE_FAILED("ORDER_CREATE_FAILED", "订单创建失败"),

    /**
     * 商品不可售卖
     */
    GOODS_NOT_FOR_SALE("GOODS_NOT_FOR_SALE", "商品不可售卖"),
    ;

    TradeErrorCode(String code, String message){
        this.code = code;
        this.message = message;
    }

    private final String code;

    private final String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
