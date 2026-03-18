package com.fw.know.go.user.domain.entity;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fw.know.go.api.user.constant.UserRole;
import com.fw.know.go.api.user.constant.UserStateEnum;
import com.fw.know.go.datasource.domain.entity.BaseEntity;
import com.fw.know.go.user.infrastructure.mapper.AesEncryptTypeHandler;
import com.github.houbb.sensitive.annotation.strategy.SensitiveStrategyPhone;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Description
 * @Date 25/2/2026 下午3:22
 * @Author Leo
 */
@Getter
@Setter
@TableName(value = "users", schema = "user_schema")
public class User extends BaseEntity {

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 密码
     */
    private String passwordHash;

    /**
     * 状态
     */
    private UserStateEnum state;

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 邀请人用户ID
     */
    private String inviterId;

    /**
     * 手机号
     */
    @SensitiveStrategyPhone
    private String telephone;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;

    /**
     * 头像地址
     */
    private String profilePhotoUrl;

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
     * 真实姓名
     */
    @TableField(typeHandler = AesEncryptTypeHandler.class)
    private String realName;

    /**
     * 身份证hash
     */
    @TableField(typeHandler = AesEncryptTypeHandler.class)
    private String idCardNo;

    /**
     * 用户角色
     */
    private UserRole userRole;

    public User register(String telephone, String nickName, String password, String inviteCode, String inviterId) {
        this.setTelephone(telephone);
        this.setNickName(nickName);
        this.setPasswordHash(DigestUtil.md5Hex(password));
        this.setInviteCode(inviteCode);
        this.setInviterId(inviterId);
        this.setState(UserStateEnum.INIT);
        this.setUserRole(UserRole.CUSTOMER);
        return this;
    }

    public User registerAdmin(String telephone, String nickName, String password){
        this.setTelephone(telephone);
        this.setNickName(nickName);
        this.setPasswordHash(DigestUtil.md5Hex(password));
        this.setState(UserStateEnum.ACTIVE);
        this.setUserRole(UserRole.ADMIN);
        return this;
    }

    public User auth(String realName, String idCard){
        this.setRealName(realName);
        this.setIdCardNo(idCard);
        this.setCertification(Boolean.TRUE);
        this.setState(UserStateEnum.AUTH);
        return this;
    }

    public User active(String bockChainUrl, String blockChainPlatform){
        this.setBlockChainPlatform(blockChainPlatform);
        this.setBlockChainUrl(blockChainUrl);
        this.setState(UserStateEnum.ACTIVE);
        return this;
    }

    public boolean canModifyInfo(){
        return state == UserStateEnum.INIT || state == UserStateEnum.AUTH || state == UserStateEnum.ACTIVE;
    }
}
