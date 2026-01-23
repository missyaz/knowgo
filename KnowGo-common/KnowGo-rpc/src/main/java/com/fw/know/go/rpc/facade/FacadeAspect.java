package com.fw.know.go.rpc.facade;

import cn.hutool.core.date.StopWatch;
import com.alibaba.fastjson2.JSON;
import com.fw.know.go.base.exception.BizException;
import com.fw.know.go.base.exception.SystemException;
import com.fw.know.go.base.response.BaseResponse;
import com.fw.know.go.base.response.ResponseCode;
import com.fw.know.go.base.utils.BeanValidator;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @Description Facade的切面处理类，统一统计进行参数校验及异常捕获
 * @Date 21/1/2026 下午5:02
 * @Author Leo
 */
@Slf4j
@Aspect
@Component
@Order(Integer.MIN_VALUE)
public class FacadeAspect {
    /**
     * 对所有标注了 {@link Facade} 注解的方法进行切面处理
     *
     * @param pjp 连接点对象，用于获取方法的参数、返回值等信息
     * @return 方法的返回值
     * @throws Exception 方法执行过程中抛出的异常
     */
    @Around("@annotation(com.fw.know.go.rpc.facade.Facade)")
    public Object facade(ProceedingJoinPoint pjp) throws Exception {
        // 1. 统计方法执行时间
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        Object[] args = pjp.getArgs();
        Class<?> returnType = method.getReturnType();

        // 循环遍历所有参数，进行参数校验
        for (Object parameter : args) {
            try {
                BeanValidator.validateObject(parameter);
            } catch (ValidationException e) {
                printErrorLog(stopWatch, method, args, "failed to validate", null, e);
                return getFailedResponse(returnType, e);
            }
        }

        try {
            // 目标方法执行
            Object response = pjp.proceed();
            enrichObject(response);
            printInfoLog(stopWatch, method, args, "end to execute", response, null);
            return response;
        }
        catch (Throwable throwable) {
            // 如果执行异常，则返回一个失败的response
            printErrorLog(stopWatch, method, args, "failed to execute", null, throwable);
            return getFailedResponse(returnType, throwable);
        }
    }

    /**
     * 将response的信息补全，主要是code和message
     * @param response response
     */
    private void enrichObject(Object response) {
        if (response instanceof BaseResponse baseResponse){
            if (baseResponse.getSuccess()){
                // 如果状态是成功，需要将未设置的responseCode设置成SUCCESS
                baseResponse.setResponseCode(ResponseCode.SUCCESS.name());
            }
            else {
                // 如果状态是失败，需要将未设置的responseCode设置成BIZ_ERROR
                baseResponse.setResponseCode(ResponseCode.BIZ_ERROR.name());
            }
        }
    }

    private Object getFailedResponse(Class<?> returnType, Throwable throwable) throws NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        // 如果返回值的类型为BaseResponse 的子类，则创建一个通用的失败响应
        if (returnType.getDeclaredConstructor().newInstance() instanceof BaseResponse response) {
            response.setSuccess(false);
            if (throwable instanceof BizException bizException){
                response.setResponseCode(bizException.getErrorCode().getCode());
                response.setResponseMessage(bizException.getErrorCode().getMessage());
            }
            else if (throwable instanceof SystemException  systemException){
                response.setResponseCode(systemException.getErrorCode().getCode());
                response.setResponseMessage(systemException.getErrorCode().getMessage());
            }
            else {
                response.setResponseCode(ResponseCode.BIZ_ERROR.name());
                response.setResponseMessage(throwable.getMessage());
            }
            return response;
        }

        log.error("failed to getFailedResponse , returnType ({}) is not instanceof BaseResponse", returnType);
        return null;
    }

    /**
     * 打印方法执行信息日志
     *
     * @param stopWatch  方法执行时间统计对象
     * @param method     被调用的方法对象
     * @param args       方法的参数数组
     * @param action     方法的执行动作描述
     * @param response   方法的返回值
     * @param throwable  方法执行过程中抛出的异常
     */
    private void printInfoLog(StopWatch stopWatch, Method method, Object[] args, String action, Object response,
                              Throwable throwable){
        try {
            log.info(getInfoMessage(stopWatch, method, args, action, response, throwable));
        }catch (Exception e){
            log.error("log failed", e);
        }
    }

    /**
     * 打印方法执行错误日志
     *
     * @param stopWatch  方法执行时间统计对象
     * @param method     被调用的方法对象
     * @param args       方法的参数数组
     * @param action     方法的执行动作描述
     * @param response   方法的返回值
     * @param throwable  方法执行过程中抛出的异常
     */
    private void printErrorLog(StopWatch stopWatch, Method method, Object[] args, String action, Object response,
                              Throwable throwable){
        try {
            log.error(getInfoMessage(stopWatch, method, args, action, response, throwable));
        }catch (Exception e){
            log.error("log failed", e);
        }
    }

    /**
     * 构建方法执行信息日志的消息体
     *
     * @param stopWatch  方法执行时间统计对象
     * @param method     被调用的方法对象
     * @param args       方法的参数数组
     * @param action     方法的执行动作描述
     * @param response   方法的返回值
     * @param throwable  方法执行过程中抛出的异常
     * @return 格式化后的方法执行信息日志消息
     */
    private String getInfoMessage(StopWatch stopWatch, Method method, Object[] args, String action, Object response, Throwable throwable) {
        StringBuilder stringBuilder = new StringBuilder(action);
        stringBuilder.append(" ,method = ").append(method.getName());
        stringBuilder.append(" ,cost = ").append(stopWatch.getTotalTimeMillis()).append(" ms");
        if (response instanceof BaseResponse){
            stringBuilder.append(" ,success = ").append(((BaseResponse) response).getSuccess());
        }
        if (throwable != null){
            stringBuilder.append(" ,success = ");
            stringBuilder.append(false);
        }
        stringBuilder.append(" ,args = ").append(JSON.toJSONString(Arrays.toString(args)));
        if (response != null){
            stringBuilder.append(" ,resp = ").append(JSON.toJSONString(response));
        }
        if (throwable != null){
            stringBuilder.append(" ,exception = ").append(throwable.getMessage());
        }

        if (response instanceof BaseResponse baseResponse){
            if (!baseResponse.getSuccess()){
                stringBuilder.append(" ,execute_failed");
            }
        }
        return stringBuilder.toString();
    }
}
