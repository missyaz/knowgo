package com.fw.know.go.api.user.constant;

import lombok.Getter;

/**
 * @Description
 * @Date 1/4/2026 下午5:47
 * @Author Leo
 */
@Getter
public enum UserType {

    /**
     * 用户
     */
    CUSTOMER("用户"),

    /**
     * 平台
     */
    PLATFORM("平台");

    private final String desc;

    UserType(String desc) {
        this.desc = desc;
    }
}
