package com.fw.know.go.api.common.constant;

/**
 * @Description
 * @Date 2/4/2026 下午1:34
 * @Author Leo
 */
public enum BusinessCode {

    /**
     * 订单
     */
    TRADE_ORDER(10, 4),
    /**
     * 支付单
     */
    PAY_ORDER(11, 1),

    /**
     * 退款单
     */
    REFUND_ORDER(12, 1),

    /**
     * 持有藏品
     */
    HELD_COLLECTION(13, 1);

    private static final int MAX_CODE = 99;

    private static final int MIN_CODE =10;

    private final int code;

    private final int tableCount;

    BusinessCode(int code, int tableCount) {
        this.code = code;
        this.tableCount = tableCount;
    }

    public int tableCount(){
        return this.tableCount;
    }

    public int code(){
        return code;
    }

    public String getCodeString(){
        return String.valueOf(code);
    }
}
