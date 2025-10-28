package com.fw.know.go.base.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * @Classname SingleResponse
 * @Description 单个数据的响应结构
 * @Date 28/10/2025 下午2:19
 * @Author Leo
 */
@Getter
@Setter
@ToString
public class SingleResponse<T> extends BaseResponse{

    @Serial
    private static final long serialVersionUID = 1L;

    private T data;

    public static <T> SingleResponse<T> of(T data){
        SingleResponse<T> singleResponse = new SingleResponse<>();
        singleResponse.setSuccess(true);
        singleResponse.setData(data);
        return singleResponse;
    }

    public static <T> SingleResponse<T> fail(String errorCode, String errorMessage){
        SingleResponse<T> singleResponse = new SingleResponse<>();
        singleResponse.setSuccess(true);
        singleResponse.setResponseCode(errorCode);
        singleResponse.setResponseMessage(errorMessage);
        return singleResponse;
    }
}
