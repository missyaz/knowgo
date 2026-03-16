package com.fw.know.go.auth.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.fw.know.go.api.notice.response.NoticeResponse;
import com.fw.know.go.api.notice.service.NoticeFacadeService;
import com.fw.know.go.api.user.request.UserQueryRequest;
import com.fw.know.go.api.user.request.UserRegisterRequest;
import com.fw.know.go.api.user.response.UserOperatorResponse;
import com.fw.know.go.api.user.service.UserFacadeService;
import com.fw.know.go.auth.exception.AuthException;
import com.fw.know.go.auth.param.LoginParam;
import com.fw.know.go.auth.param.RegisterParam;
import com.fw.know.go.auth.service.AuthService;
import com.fw.know.go.auth.vo.LoginVO;
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

    private final AuthService authService;

    @GetMapping("/sendCaptcha")
    public Result<Boolean> sendCaptcha(@IsMobile String telephone) {
        return Result.success(authService.sendCaptcha(telephone));
    }

    @PostMapping("/register")
    public Result<Boolean> register(@Valid @RequestBody RegisterParam registerParam) {
        return Result.success(authService.register(registerParam));
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginParam loginParam){
        return Result.success(authService.login(loginParam));
    }

    @PostMapping("/logout")
    public Result<Boolean> logout(){
        StpUtil.logout();
        return Result.success(true);
    }
}
