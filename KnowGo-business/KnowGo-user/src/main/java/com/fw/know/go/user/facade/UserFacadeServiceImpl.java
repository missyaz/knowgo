package com.fw.know.go.user.facade;

import com.fw.know.go.api.user.request.UserRegisterRequest;
import com.fw.know.go.api.user.response.UserOperatorResponse;
import com.fw.know.go.api.user.service.UserFacadeService;
import com.fw.know.go.rpc.facade.Facade;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @Description
 * @Date 24/2/2026 下午5:16
 * @Author Leo
 */
@DubboService(version = "1.0.0")
public class UserFacadeServiceImpl implements UserFacadeService {

    @Override
    @Facade
    public UserOperatorResponse register(UserRegisterRequest userRegisterRequest) {
        return null;
    }
}
