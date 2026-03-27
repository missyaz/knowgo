package com.fw.know.go.auth.controller;

import com.fw.know.go.api.goods.service.GoodsFacadeService;
import com.fw.know.go.auth.intrastructure.constant.TokenSceneEnum;
import com.fw.know.go.auth.intrastructure.exception.AuthErrorCode;
import com.fw.know.go.auth.intrastructure.exception.AuthException;
import com.fw.know.go.auth.service.AuthService;
import com.fw.know.go.web.vo.Result;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @Description
 * @Date 26/3/2026 下午2:59
 * @Author Leo
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired
    private AuthService authService;

    @GetMapping("/get")
    public Result<String> get(@NotBlank String scene, @NotBlank String key){
        return Result.success(authService.getToken(scene, key));
    }
}
