package com.fw.know.go.api.user.service;

import com.fw.know.go.api.user.request.UserRegisterRequest;
import com.fw.know.go.api.user.response.UserOperatorResponse;

/**
 * @Description
 * @Date 24/2/2026 上午11:01
 * @Author Leo
 */
public interface UserFacadeService {

    /**
     * 用户注册
     * @param userRegisterRequest 用户注册参数
     * @return 相应结果
     */
    UserOperatorResponse register(UserRegisterRequest userRegisterRequest);
}
