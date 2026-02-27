package com.fw.know.go.auth.controller;

import cn.hutool.core.util.StrUtil;
import com.fw.know.go.api.notice.response.NoticeResponse;
import com.fw.know.go.api.notice.service.NoticeFacadeService;
import com.fw.know.go.api.user.request.UserRegisterRequest;
import com.fw.know.go.api.user.response.UserOperatorResponse;
import com.fw.know.go.api.user.service.UserFacadeService;
import com.fw.know.go.auth.exception.AuthException;
import com.fw.know.go.auth.param.RegisterParam;
import com.fw.know.go.base.validator.IsMobile;
import com.fw.know.go.web.vo.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import static com.fw.know.go.api.notice.constant.NoticeConstant.CAPTCHA_KEY_PREFIX;
import static com.fw.know.go.auth.exception.AuthErrorCode.*;

/**
 * @Description
 * @Date 21/1/2026 下午2:03
 * @Author Leo
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @DubboReference(version = "1.0.0")
    private NoticeFacadeService noticeFacadeService;

    @DubboReference(version = "1.0.0")
    private UserFacadeService userFacadeService;

    /**
     * 默认登录超时时间：7天
     */
    private static final Integer DEFAULT_LOGIN_SESSION_TIMEOUT = 60 * 60 * 24 * 7;

    @GetMapping("/sendCaptcha")
    public Result<Boolean> sendCaptcha(@IsMobile String telephone) {
        NoticeResponse noticeResponse = noticeFacadeService.generateAndSendSmsCaptcha(telephone);
        return Result.success(noticeResponse.getSuccess());
    }

    @PostMapping("/register")
    public Result<Boolean> register(@Valid @RequestBody RegisterParam registerParam) {
        // 验证码校验
        String cachedCode = redisTemplate.opsForValue().get(CAPTCHA_KEY_PREFIX + registerParam.getTelephone());
        if (!StrUtil.equalsIgnoreCase(cachedCode, registerParam.getCaptcha())){
            throw new AuthException(VERIFICATION_CODE_WRONG);
        }

        // 注册
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setTelephone(registerParam.getTelephone());
        userRegisterRequest.setInviteCode(registerParam.getInviteCode());

        UserOperatorResponse registerResult = userFacadeService.register(userRegisterRequest);
        if (registerResult.getSuccess()){
            return Result.success(true);
        }
        return Result.error(registerResult.getResponseCode(), registerResult.getResponseMessage());
    }
}
