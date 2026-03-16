package com.fw.know.go.auth.vo;

import cn.dev33.satoken.stp.StpUtil;
import com.fw.know.go.api.user.response.data.UserInfo;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Description
 * @Date 16/3/2026 上午10:58
 * @Author Leo
 */
@Data
public class LoginVO implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户标识，如用户ID
     */
    private String userId;

    /**
     * 访问令牌
     */
    private String token;

    /**
     * 令牌过期时间
     */
    private Long tokenExpireTime;

    public LoginVO(UserInfo userInfo){
        this.userId = userInfo.getUserId().toString();
        this.token = StpUtil.getTokenValue();
        this.tokenExpireTime = StpUtil.getTokenSessionTimeout();
    }
}
