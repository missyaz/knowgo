package com.fw.know.go.tcc.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Description 事务请求参数
 * @Date 16/4/2026 下午3:22
 * @Author Leo
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TccRequest implements Serializable {

    @Serial
    public static final long serialVersionUID = 1L;

    /**
     * 事务ID
     */
    private String transactionId;

    /**
     * 业务场景
     */
    private String businessScene;

    /**
     * 业务模块
     */
    private String businessModule;
}
