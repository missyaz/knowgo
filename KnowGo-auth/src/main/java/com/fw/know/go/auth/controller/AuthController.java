package com.fw.know.go.auth.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.fw.know.go.auth.param.LoginParam;
import com.fw.know.go.auth.param.RegisterParam;
import com.fw.know.go.auth.service.AuthService;
import com.fw.know.go.auth.vo.LoginVO;
import com.fw.know.go.base.validator.IsMobile;
import com.fw.know.go.web.vo.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
