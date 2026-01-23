package com.fw.know.go.auth.param;

import com.fw.know.go.base.validator.IsMobile;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description
 * @Date 22/1/2026 上午10:11
 * @Author Leo
 */
@Getter
@Setter
public class RegisterParam {

    /**
     * 手机号
     */
    @IsMobile
    private String telephone;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String captcha;

    /**
     * 邀请码
     */
    private String inviteCode;
}
