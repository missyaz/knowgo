package com.fw.know.go.api.user.response;

import com.fw.know.go.api.user.response.data.UserInfo;
import com.fw.know.go.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description 用户操作响应
 * @Date 24/2/2026 上午11:06
 * @Author Leo
 */
@Getter
@Setter
public class UserOperatorResponse extends BaseResponse {

    /**
     * 用户信息
     */
    private UserInfo user;
}
