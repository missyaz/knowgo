package com.fw.know.go.api.user.constant;

/**
 * @Description 用户操作类型
 * @Date 27/2/2026 上午9:53
 * @Author Leo
 */
public enum UserOperateTypeEnum {

    /**
     * 冻结
     */
    FREEZE,

    /**
     * 解冻
     */
    UNFREEZE,

    /**
     * 登录
     */
    LOGIN,
    /**
     * 注册
     */
    REGISTER,
    /**
     * 激活
     */
    ACTIVE,
    /**
     * 实名认证
     */
    AUTH,
    /**
     * 修改信息
     */
    MODIFY
    ;
}
