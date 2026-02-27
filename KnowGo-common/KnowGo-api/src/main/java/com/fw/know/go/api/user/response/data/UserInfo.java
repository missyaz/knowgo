package com.fw.know.go.api.user.response.data;

import com.fw.know.go.api.user.constant.UserRole;
import com.fw.know.go.api.user.constant.UserStateEnum;
import com.github.houbb.sensitive.annotation.strategy.SensitiveStrategyPhone;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.util.Date;

/**
 * @Description 用户信息
 * @Date 24/2/2026 上午11:07
 * @Author Leo
 */
@Getter
@Setter
@NoArgsConstructor
public class UserInfo extends BasicUserInfo {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 手机号
     */
    @SensitiveStrategyPhone
    private String telephone;

    /**
     * 状态
     *
     * @see UserStateEnum
     */
    private UserStateEnum state;

    /**
     * 区块链地址
     */
    private String blockChainUrl;

    /**
     * 区块链平台
     */
    private String blockChainPlatform;

    /**
     * 实名认证
     */
    private Boolean certification;

    /**
     * 用户角色
     */
    private UserRole userRole;

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 注册时间
     */
    private Date createTime;

    public boolean userCanBuy(){

        if (this.getUserRole() != null && !this.getUserRole().equals(UserRole.CUSTOMER)){
            // 不是普通用户
            return false;
        }
        // 判断卖家状态
        if (this.getState() != null && !this.getState().equals(UserStateEnum.ACTIVE.name())){
            return false;
        }
        if (this.getState() != null && !this.getCertification()){
            return false;
        }
        return true;
    }
}
