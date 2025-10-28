package com.fw.know.go.web.vo;

import lombok.Getter;
import lombok.Setter;

import static com.fw.know.go.base.response.ResponseCode.SUCCESS;

/**
 * @Classname Result
 * @Description Web通用返回类
 * @Date 28/10/2025 上午11:21
 * @Author Leo
 */
@Getter
@Setter
public class Result<T> {

    /**
     * 状态码
     */
    private String code;

     /**
      * 是否成功
      */
    private Boolean success;

    /**
     * 消息描述
     */
    private String message;

    /**
     * 数据，可以是任何类型的 VO
     */
    private T data;

    public Result(){}

    public Result(Boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public Result(Boolean success, String code, String message, T data) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(true, SUCCESS.name(), SUCCESS.name(), data);
    }

    public static <T> Result<T> error(String errorCode, String errorMsg) {
        return new Result<>(false, errorCode, errorMsg, null);
    }
}
