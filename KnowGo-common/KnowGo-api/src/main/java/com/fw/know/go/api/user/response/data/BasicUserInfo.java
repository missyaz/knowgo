package com.fw.know.go.api.user.response.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Description 简单的用户信息，只返回部分字段，避免过多不该返回的信息被返回
 * @Date 24/2/2026 上午11:13
 * @Author Leo
 */
@Getter
@Setter
@NoArgsConstructor
public class BasicUserInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 昵称
     */
    private String niceName;

    /**
     * 头像地址
     */
    private String profilePhotoUrl;
}
