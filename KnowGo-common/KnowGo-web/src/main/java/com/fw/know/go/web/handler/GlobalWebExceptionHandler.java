package com.fw.know.go.web.handler;

import cn.hutool.core.map.MapUtil;
import com.fw.know.go.base.exception.BizException;
import com.fw.know.go.base.exception.SystemException;
import com.fw.know.go.web.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

import static com.fw.know.go.base.response.ResponseCode.SYSTEM_ERROR;


/**
 * @Classname GlobalWebExceptionHandler
 * @Description 全局Web异常处理器
 * @Date 28/10/2025 下午1:17
 * @Author Leo
 */
@Slf4j
@ControllerAdvice
public class GlobalWebExceptionHandler {

    /**
     * 处理方法参数无效异常
     *
     * @param ex 方法参数无效异常
     * @return 包含字段名和错误信息的映射
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException occurred: {}", ex.getMessage(), ex);
        Map<String, String> errors = MapUtil.newHashMap(1);
        ex.getBindingResult().getAllErrors().forEach(error -> {
            errors.put(((FieldError) error).getField(), error.getDefaultMessage());
        });
        return errors;
    }

    /**
     * 处理业务异常
     *
     * @param ex 业务异常
     * @return 包含错误信息的映射
     */
    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result<Object> handleBizException(BizException ex) {
        log.error("BizException occurred: {}", ex.getMessage(), ex);
        Result<Object> result = new Result<>();
        result.setCode(ex.getErrorCode().getCode());
        if (ex.getMessage() == null){
            result.setMessage(ex.getErrorCode().getMessage());
        } else {
            result.setMessage(ex.getMessage());
        }
        result.setSuccess(false);
        return result;
    }

     /**
     * 处理系统异常
     *
     * @param ex 系统异常
     * @return 包含错误信息的映射
     */
    @ExceptionHandler(SystemException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result<Object> handleSystemException(SystemException ex) {
        log.error("SystemException occurred: {}", ex.getMessage(), ex);
        Result<Object> result = new Result<>();
        result.setCode(ex.getErrorCode().getCode());
        if (ex.getMessage() == null){
            result.setMessage(ex.getErrorCode().getMessage());
        } else {
            result.setMessage(ex.getMessage());
        }
        result.setSuccess(false);
        return result;
    }

    /**
     * 处理所有其他异常
     *
     * @param ex 其他异常
     * @return 包含错误信息的映射
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result<Object> handleThrowable(Throwable ex) {
        log.error("Throwable occurred: {}", ex.getMessage(), ex);
        Result<Object> result = new Result<>();
        result.setCode(SYSTEM_ERROR.name());
        result.setMessage("哎呀，当前网络比较繁忙，请稍后再试");
        result.setSuccess(false);
        return result;
    }
}