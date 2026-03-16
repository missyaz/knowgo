package com.fw.know.go.api.user.service;

import com.fw.know.go.api.user.request.UserQueryRequest;
import com.fw.know.go.api.user.request.UserRegisterRequest;
import com.fw.know.go.api.user.response.UserOperatorResponse;
import com.fw.know.go.api.user.response.UserQueryResponse;
import com.fw.know.go.api.user.response.data.UserInfo;

/**
 * @Description
 * @Date 24/2/2026 上午11:01
 * @Author Leo
 */
public interface UserFacadeService {

    /**
     * 查询用户信息
     * @param userQueryRequest 查询参数
     * @return 相应结果
     */
    UserQueryResponse<UserInfo> query(UserQueryRequest userQueryRequest);

    /**
     * 用户注册
     * @param userRegisterRequest 用户注册参数
     * @return 相应结果
     */
    UserOperatorResponse register(UserRegisterRequest userRegisterRequest);
}
