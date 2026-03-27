package com.fw.know.go.auth.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import cn.hutool.core.util.StrUtil;
import com.fw.know.go.api.goods.constant.GoodsType;
import com.fw.know.go.api.goods.model.BaseGoodsVO;
import com.fw.know.go.api.goods.service.GoodsFacadeService;
import com.fw.know.go.api.notice.service.NoticeFacadeService;
import com.fw.know.go.api.user.request.UserQueryRequest;
import com.fw.know.go.api.user.request.UserRegisterRequest;
import com.fw.know.go.api.user.response.UserOperatorResponse;
import com.fw.know.go.api.user.response.UserQueryResponse;
import com.fw.know.go.api.user.response.data.UserInfo;
import com.fw.know.go.api.user.service.UserFacadeService;
import com.fw.know.go.auth.intrastructure.constant.TokenSceneEnum;
import com.fw.know.go.auth.intrastructure.exception.AuthErrorCode;
import com.fw.know.go.auth.intrastructure.exception.AuthException;
import com.fw.know.go.auth.param.LoginParam;
import com.fw.know.go.auth.param.RegisterParam;
import com.fw.know.go.auth.vo.LoginVO;
import com.fw.know.go.web.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.fw.know.go.api.notice.constant.NoticeConstant.CAPTCHA_KEY_PREFIX;
import static com.fw.know.go.auth.intrastructure.exception.AuthErrorCode.REGISTER_ERROR;
import static com.fw.know.go.auth.intrastructure.exception.AuthErrorCode.VERIFICATION_CODE_WRONG;
import static com.fw.know.go.cache.constant.CacheConstant.CACHE_KEY_SEPARATOR;
import static com.fw.know.go.web.util.TokenUtil.TOKEN_PREFIX;

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

    @DubboReference(version = "1.0.0")
    private GoodsFacadeService goodsFacadeService;

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
        StpUtil.getSession().set(userInfo.getUserId().toString(), userInfo);
        return new LoginVO(userInfo);
    }

    public String getToken(String scene, String key){
        /**
         * 检查下key是不是合法的值（存在的商品id），如果不合法，拒绝生成token，避免攻击者传入一堆随机的key来生成token。
         * 如果做的再好点，商品id不用自增id，而是雪花算法等方式生成，避免攻击者穷举
         *
         * 但是在后面校验token的时候，还是有个问题，那就是我们其实没有校验token对应的商品和下单的商品是不是同一个。这块大家可以自行实现一下。
         */
        TokenSceneEnum tokenScene = Arrays.stream(TokenSceneEnum.values()).filter(tokenSceneEnum -> tokenSceneEnum.getScene().equals(scene))
                .findFirst()
                .orElseThrow(() -> new AuthException(AuthErrorCode.TOKEN_SCENE_NOT_EXIST));

        BaseGoodsVO goods = goodsFacadeService.getGoods(key, getGoodsType(tokenScene));
        if (goods == null){
            throw new AuthException(AuthErrorCode.TOKEN_KEY_IS_ILLEGAL);
        }

        if (StpUtil.isLogin()){
            String userId = StpUtil.getLoginIdAsString();
            // token:buy:29:10085
            String tokenKey = TOKEN_PREFIX + scene + CACHE_KEY_SEPARATOR + userId + CACHE_KEY_SEPARATOR + key;
            String tokenValue = TokenUtil.getTokenValueByKey(tokenKey);
            //key：token:buy:29:10085
            //value：YZdkYfQ8fy7biSTsS5oZrbsB8eN7dHPgtCV0dw/36AHSfDQzWOj+ULNEcMluHvep/txjP+BqVRH3JlprS8tWrQ==
            redisTemplate.opsForValue().set(tokenKey, tokenValue, 30, TimeUnit.MINUTES);
            return tokenValue;
        }
        throw new AuthException(AuthErrorCode.USER_NOT_LOGIN);
    }

    /**
     * 根据令牌场景枚举获取对应的商品类型
     * @param tokenSceneEnum 令牌场景枚举，表示不同的购买场景
     * @return 返回对应的商品类型枚举
     * @throws AuthException 当传入的令牌场景不存在时抛出异常
     */
    private GoodsType getGoodsType(TokenSceneEnum tokenSceneEnum){
    // 使用switch表达式根据令牌场景枚举返回对应的商品类型
        return switch (tokenSceneEnum) {
        // 当场景为购买收藏品时，返回收藏品类型
            case BUY_COLLECTION -> GoodsType.COLLECTION;
        // 当场景为购买盲盒时，返回盲盒类型
            case BUY_BLIND_BOX -> GoodsType.BLIND_BOX;
        // 默认情况，抛出令牌场景不存在的异常
            default -> throw new AuthException(AuthErrorCode.TOKEN_SCENE_NOT_EXIST);
        };
    }
}
