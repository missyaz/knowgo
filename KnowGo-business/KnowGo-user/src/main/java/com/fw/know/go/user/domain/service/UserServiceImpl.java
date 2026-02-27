package com.fw.know.go.user.domain.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fw.know.go.api.user.constant.UserOperateTypeEnum;
import com.fw.know.go.api.user.response.UserOperatorResponse;
import com.fw.know.go.base.exception.BizException;
import com.fw.know.go.base.exception.RepoErrorCode;
import com.fw.know.go.user.domain.entity.User;
import com.fw.know.go.user.infrastructure.exception.UserErrorCode;
import com.fw.know.go.user.infrastructure.exception.UserException;
import com.fw.know.go.user.infrastructure.mapper.UserMapper;
import com.fw.know.go.user.infrastructure.mapper.UserMapperService;
import com.fw.know.go.user.infrastructure.mapper.UserOperatorStreamMapperService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.fw.know.go.user.infrastructure.exception.UserErrorCode.*;

/**
 * @Description 用户领域核心业务类
 * @Date 25/2/2026 上午10:39
 * @Author Leo
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements InitializingBean {

    private static final String DEFAULT_NICK_NAME_PREFIX = "藏家_";

    @Autowired
    private UserMapperService userMapperService;

    @Autowired
    private UserOperatorStreamService userOperatorStreamService;

    @Transactional(rollbackFor = Exception.class)
    public UserOperatorResponse register(String telephone, String inviteCode){
        String defaultNickName;
        String randomString;
        // 昵称、邀请码重复时，使用默认昵称
        do {
            randomString = RandomUtil.randomString(6).toUpperCase();
            // 前缀 + 6位随机数 + 手机号后四位
            defaultNickName = DEFAULT_NICK_NAME_PREFIX + randomString + telephone.substring(7, 11);
        } while (nickNameExist(defaultNickName) || inviteCodeExist(randomString));

        // 获取邀请人ID
        String inviterId = null;
        if (StrUtil.isNotBlank(inviteCode)){
            User inviter = userMapperService.findByInviteCode(inviteCode);
            if (inviter != null){
                inviterId = inviter.getInviterId();
            }
        }

        // 使用手机号当密码
        User user = this.register(telephone, defaultNickName, telephone, randomString, inviterId);
        Assert.notNull(user, UserErrorCode.USER_OPERATE_FAILED.getCode());

        // 校验昵称
        // 校验验证码

        // 加入流水
        Long streamResult = userOperatorStreamService.insertStream(user, UserOperateTypeEnum.REGISTER);
        Assert.notNull(streamResult, () -> new BizException(RepoErrorCode.UPDATE_FAILED));

        UserOperatorResponse userOperatorResponse = new UserOperatorResponse();
        userOperatorResponse.setSuccess(true);

        return userOperatorResponse;
    }

    /**
     * 注册
     * @param telephone 手机号
     * @param nickName 昵称
     * @param password 密码
     * @param inviteCode 邀请码
     * @param inviterId 邀请人ID
     * @return 用户
     */
    private User register(String telephone, String nickName, String password, String inviteCode, String inviterId) {
        if (userMapperService.findByTelephone(telephone) != null){
            throw new UserException(DUPLICATE_TELEPHONE_NUMBER);
        }

        User user = new User();
        user.register(telephone, nickName, password, inviteCode, inviterId);
        return save(user) ? user : null;
    }


    public boolean nickNameExist(String nickName){
        // 如果布隆过滤器中存在，再进行数据库二次判断
        return false;
    }

    public boolean inviteCodeExist(String inviteCode){
        // 如果布隆过滤器中存在，再进行数据库二次判断
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
