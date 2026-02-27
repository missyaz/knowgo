package com.fw.know.go.api.user.constant;

/**
 * @Description 用户状态
 * @Date 24/2/2026 下午2:13
 * @Author Leo
 */
public enum UserStateEnum {

    /**
     * 创建成功
     */
    INIT,
    /**
     * 实名认证
     */
    AUTH,
    /**
     * 上链成功
     */
    ACTIVE,

    /**
     * 冻结
     */
    FROZEN;
}
