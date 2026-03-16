package com.fw.know.go.api.user.response;

import com.fw.know.go.base.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * @Description
 * @Date 16/3/2026 下午4:21
 * @Author Leo
 */
@Getter
@Setter
@ToString
public class UserQueryResponse<T> extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    private T data;
}
