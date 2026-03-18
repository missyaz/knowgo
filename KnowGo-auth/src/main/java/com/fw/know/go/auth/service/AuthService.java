package com.fw.know.go.auth.service;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import cn.hutool.core.util.StrUtil;
import com.fw.know.go.api.notice.response.NoticeResponse;
import com.fw.know.go.api.notice.service.NoticeFacadeService;
import com.fw.know.go.api.user.request.UserQueryRequest;
import com.fw.know.go.api.user.request.UserRegisterRequest;
import com.fw.know.go.api.user.response.UserOperatorResponse;
import com.fw.know.go.api.user.response.UserQueryResponse;
import com.fw.know.go.api.user.response.data.UserInfo;
import com.fw.know.go.api.user.service.UserFacadeService;
import com.fw.know.go.auth.exception.AuthErrorCode;
import com.fw.know.go.auth.exception.AuthException;
import com.fw.know.go.auth.param.LoginParam;
import com.fw.know.go.auth.param.RegisterParam;
import com.fw.know.go.auth.vo.LoginVO;
import com.fw.know.go.web.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static com.fw.know.go.api.notice.constant.NoticeConstant.CAPTCHA_KEY_PREFIX;
import static com.fw.know.go.auth.exception.AuthErrorCode.REGISTER_ERROR;
import static com.fw.know.go.auth.exception.AuthErrorCode.VERIFICATION_CODE_WRONG;

/**
 * @Description
 * @Date 16/3/2026 下午2:41
 * @Author Leo
 */
@Slf4j
@Service
public class AuthService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @DubboReference(version = "1.0.0")
    private NoticeFacadeService noticeFacadeService;

    @DubboReference(version = "1.0.0")
    private UserFacadeService userFacadeService;

    private static final String ROOT_CAPTCHA = "8888";

    /**
     * 默认登录超时时间：7天
     */
    private static final Integer DEFAULT_LOGIN_SESSION_TIMEOUT = 60 * 60 * 24 * 7;

    public Boolean sendCaptcha(String telephone){
        return noticeFacadeService.generateAndSendSmsCaptcha(telephone).getSuccess();
    }


    /**
     * 验证码验证方法
     * @param telephone 手机号码，用于标识需要验证的用户
     * @param captcha 用户输入的验证码
     */
    private void validateCaptcha(String telephone, String captcha) {
        // 保留原有的万能验证码逻辑
        if (ROOT_CAPTCHA.equals(captcha)) {
            return;
        }
        String cachedCode = redisTemplate.opsForValue().get(CAPTCHA_KEY_PREFIX + telephone);
        if (!StrUtil.equalsIgnoreCase(cachedCode, captcha)) {
            throw new AuthException(VERIFICATION_CODE_WRONG);
        }
    }

    public Boolean register(RegisterParam registerParam){
        // 校验验证码
        validateCaptcha(registerParam.getTelephone(), registerParam.getCaptcha());

        // 注册
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setTelephone(registerParam.getTelephone());
        userRegisterRequest.setInviteCode(registerParam.getInviteCode());

        UserOperatorResponse registerResult = userFacadeService.register(userRegisterRequest);
        if (!registerResult.getSuccess()){
            throw new AuthException(REGISTER_ERROR);
        }
        return true;
    }

    public LoginVO login(LoginParam loginParam){
        validateCaptcha(loginParam.getTelephone(), loginParam.getCaptcha());
        // 判断是注册还是登录
        // 查询用户信息
        UserQueryRequest userQueryRequest = new UserQueryRequest(loginParam.getTelephone());
        UserQueryResponse<UserInfo> userQueryResponse = userFacadeService.query(userQueryRequest);
        UserInfo userInfo = userQueryResponse.getData();
        if (userInfo == null){
            // 需要注册
            UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
            userRegisterRequest.setTelephone(loginParam.getTelephone());
            userRegisterRequest.setInviteCode(loginParam.getInviteCode());

            UserOperatorResponse response = userFacadeService.register(userRegisterRequest);
            if (!response.getSuccess()){
                throw new AuthException(REGISTER_ERROR);
            }
            userQueryResponse = userFacadeService.query(userQueryRequest);
            userInfo = userQueryResponse.getData();
        }
        StpUtil.login(userInfo.getUserId(),
                new SaLoginParameter().setIsLastingCookie(loginParam.getRememberMe()).setTimeout(DEFAULT_LOGIN_SESSION_TIMEOUT));
//        StpUtil.getSession().set(userInfo.getUserId().toString(), userInfo);
        return new LoginVO(userInfo);
    }
}
