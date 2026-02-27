package com.fw.know.go.api.user.request;

import lombok.*;

/**
 * @Description 用户注册请求参数
 * @Date 24/2/2026 上午11:04
 * @Author Leo
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 密码
     */
    private String password;
}
