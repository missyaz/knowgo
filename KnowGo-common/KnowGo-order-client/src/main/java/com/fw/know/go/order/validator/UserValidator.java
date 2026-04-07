package com.fw.know.go.order.validator;

import com.fw.know.go.api.order.request.OrderCreateRequest;
import com.fw.know.go.api.user.constant.UserRole;
import com.fw.know.go.api.user.constant.UserStateEnum;
import com.fw.know.go.api.user.request.UserQueryRequest;
import com.fw.know.go.api.user.response.UserQueryResponse;
import com.fw.know.go.api.user.response.data.UserInfo;
import com.fw.know.go.api.user.service.UserFacadeService;
import com.fw.know.go.order.OrderException;

import static com.fw.know.go.api.order.constant.OrderErrorCode.*;

/**
 * @Description 用户校验器
 * @Date 7/4/2026 上午10:18
 * @Author Leo
 */
public class UserValidator extends BaseOrderCreateValidator{

    private final UserFacadeService userFacadeService;

    public UserValidator(UserFacadeService userFacadeService) {
        this.userFacadeService = userFacadeService;
    }

    @Override
    protected void doValidate(OrderCreateRequest request) throws OrderException {
        String buyerId = request.getBuyerId();
        UserQueryRequest userQueryRequest = new UserQueryRequest(Long.valueOf(buyerId));
        UserQueryResponse<UserInfo> queryResponse = userFacadeService.query(userQueryRequest);
        if (queryResponse.getSuccess() && queryResponse.getData() != null){
            UserInfo userInfo = queryResponse.getData();
            if (userInfo.getUserRole() != null && !userInfo.getUserRole().equals(UserRole.CUSTOMER)){
                // 不是普通买家
                throw new OrderException(BUYER_IS_PLATFORM_USER);
            }
            // 判断买家状态
            if (userInfo.getState() != null && !userInfo.getState().equals(UserStateEnum.ACTIVE)){
                throw new OrderException(BUYER_STATUS_ABNORMAL);
            }
            // 判断买家是否实名认证
            if (userInfo.getState() != null && !userInfo.getCertification()){
                throw new OrderException(BUYER_NOT_AUTH);
            }
        }
    }
}
