package com.fw.know.go.api.pay.constant;

import lombok.Getter;

/**
 * @Description
 * @Date 1/4/2026 下午5:49
 * @Author Leo
 */
@Getter
public enum PayChannel {

    /**
     * 支付宝
     */
    ALIPAY("支付宝"),

    /**
     * 微信
     */
    WECHAT("微信"),

    /**
     * MOCK
     */
    MOCK("MOCK");

    private final String value;

    PayChannel(String value) {
        this.value = value;
    }
}
