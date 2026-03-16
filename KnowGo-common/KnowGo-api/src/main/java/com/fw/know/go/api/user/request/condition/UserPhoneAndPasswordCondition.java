package com.fw.know.go.api.user.request.condition;

import lombok.*;

import java.io.Serial;

/**
 * @Description
 * @Date 16/3/2026 下午1:17
 * @Author Leo
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserPhoneAndPasswordCondition implements UserQueryCondition{

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户手机号
     */
    private String telephone;

    /**
     * 用户密码
     */
    private String password;
}
