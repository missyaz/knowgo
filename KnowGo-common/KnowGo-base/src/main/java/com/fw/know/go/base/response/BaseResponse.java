package com.fw.know.go.base.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Classname BaseResponse
 * @Description 通用出参
 * @Date 28/10/2025 下午1:49
 * @Author Leo
 */
@Getter
@Setter
@ToString
public class BaseResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Boolean success;

    private String responseCode;

    private String responseMessage;
}
