package com.fw.know.go.user.facade;

import com.fw.know.go.api.user.request.UserQueryRequest;
import com.fw.know.go.api.user.request.UserRegisterRequest;
import com.fw.know.go.api.user.request.condition.UserIdQueryCondition;
import com.fw.know.go.api.user.request.condition.UserPhoneAndPasswordCondition;
import com.fw.know.go.api.user.request.condition.UserPhoneQueryCondition;
import com.fw.know.go.api.user.response.UserOperatorResponse;
import com.fw.know.go.api.user.response.UserQueryResponse;
import com.fw.know.go.api.user.response.data.UserInfo;
import com.fw.know.go.api.user.service.UserFacadeService;
import com.fw.know.go.rpc.facade.Facade;
import com.fw.know.go.user.domain.entity.User;
import com.fw.know.go.user.domain.entity.convertor.UserConvertior;
import com.fw.know.go.user.domain.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description
 * @Date 24/2/2026 下午5:16
 * @Author Leo
 */
@DubboService(version = "1.0.0")
public class UserFacadeServiceImpl implements UserFacadeService {

    @Autowired
    private UserService userService;

    @Override
    public UserQueryResponse<UserInfo> query(UserQueryRequest userQueryRequest) {
        User user = switch (userQueryRequest.getUserQueryCondition()) {
            case UserIdQueryCondition userIdQueryCondition -> userService.getById(userIdQueryCondition.getUserId());
            case UserPhoneQueryCondition userPhoneQueryCondition ->
                    userService.getByTelephone(userPhoneQueryCondition.getTelephone());
            case UserPhoneAndPasswordCondition userPhoneAndPasswordCondition ->
                    userService.getByPhoneAndPassword(userPhoneAndPasswordCondition.getTelephone(),
                            userPhoneAndPasswordCondition.getPassword());
            default -> throw new UnsupportedOperationException(userQueryRequest.getUserQueryCondition() + "'' is not " +
                    "supported");
        };

        UserQueryResponse<UserInfo> userQueryResponse = new UserQueryResponse<>();
        userQueryResponse.setSuccess(true);
        UserInfo userInfo = UserConvertior.INSTANCE.mapToVo(user);
        userQueryResponse.setData(userInfo);
        return userQueryResponse;
    }

    @Override
    @Facade
    public UserOperatorResponse register(UserRegisterRequest userRegisterRequest) {
        return userService.register(userRegisterRequest.getTelephone(), userRegisterRequest.getInviteCode());
    }
}
