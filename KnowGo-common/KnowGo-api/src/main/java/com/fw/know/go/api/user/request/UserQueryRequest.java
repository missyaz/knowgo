package com.fw.know.go.api.user.request;

import com.fw.know.go.api.user.request.condition.UserIdQueryCondition;
import com.fw.know.go.api.user.request.condition.UserPhoneAndPasswordCondition;
import com.fw.know.go.api.user.request.condition.UserPhoneQueryCondition;
import com.fw.know.go.api.user.request.condition.UserQueryCondition;
import com.fw.know.go.base.request.BaseRequest;
import lombok.*;

/**
 * @Description 用户查询请求对象，内部查询
 * @Date 16/3/2026 下午1:08
 * @Author Leo
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserQueryRequest extends BaseRequest {

    private UserQueryCondition userQueryCondition;

    public UserQueryRequest(Long userId) {
        UserIdQueryCondition userIdQueryCondition = new UserIdQueryCondition();
        userIdQueryCondition.setUserId(userId);
        this.userQueryCondition = userIdQueryCondition;
    }

    public UserQueryRequest(String telephone) {
        UserPhoneQueryCondition userPhoneQueryCondition = new UserPhoneQueryCondition();
        userPhoneQueryCondition.setTelephone(telephone);
        this.userQueryCondition = userPhoneQueryCondition;
    }

    public UserQueryRequest(String telephone, String password) {
        UserPhoneAndPasswordCondition userPhoneAndPasswordCondition = new UserPhoneAndPasswordCondition();
        userPhoneAndPasswordCondition.setTelephone(telephone);
        userPhoneAndPasswordCondition.setPassword(password);
        this.userQueryCondition = userPhoneAndPasswordCondition;
    }
}
