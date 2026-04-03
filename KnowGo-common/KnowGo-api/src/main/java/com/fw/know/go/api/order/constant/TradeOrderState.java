package com.fw.know.go.api.order.constant;

/**
 * @Description 订单状态
 * @Date 1/4/2026 下午5:52
 * @Author Leo
 */
public enum TradeOrderState {

    /**
     * 订单创建
     */
    CREATE,

    /**
     * 订单确认
     */
    CONFIRM,
    /**
     * 已付款
     */
    PAID,
    /**
     * 交易成功
     */
    FINISH,
    /**
     * 订单关闭
     */
    CLOSED,
    /**
     * 废单，用户看不到
     */
    DISCARD;
}
