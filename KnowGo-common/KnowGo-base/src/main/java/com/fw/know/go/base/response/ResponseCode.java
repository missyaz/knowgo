package com.fw.know.go.base.response;

import lombok.Getter;

/**
 * @Classname ResponseCode
 * @Description 返回值状态码
 * @Date 28/10/2025 下午1:10
 * @Author Leo
 */
@Getter
public enum ResponseCode {

    /**
     * 成功
     */
    SUCCESS,

    /**
     * 系统错误
     */
    SYSTEM_ERROR,

    /**
     * 业务错误
     */
    BIZ_ERROR;
}
