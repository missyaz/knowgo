package com.fw.know.go.auth.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description
 * @Date 16/3/2026 上午10:57
 * @Author Leo
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoginParam extends RegisterParam{

    /**
     * 记住我
     */
    private Boolean rememberMe;
}

