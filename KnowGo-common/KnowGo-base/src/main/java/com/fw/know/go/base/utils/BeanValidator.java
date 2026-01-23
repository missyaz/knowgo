package com.fw.know.go.base.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import org.hibernate.validator.HibernateValidator;

import java.util.Set;

/**
 * @Description 参数校验工具
 * @Date 21/1/2026 下午5:22
 * @Author Leo
 */
public class BeanValidator {

    private static final Validator VALIDATOR = Validation.byProvider(HibernateValidator.class).configure().failFast(true)
            .buildValidatorFactory().getValidator();

    /**
     * 校验对象的参数是否符合校验规则
     * @param object 待校验的对象
     * @param groups 校验组，用于指定校验规则
     * @throws ValidationException 如果校验失败，抛出校验异常
     */
    public static void validateObject(Object object, Class<?>... groups) throws ValidationException {
        Set<ConstraintViolation<Object>> constraintViolations = VALIDATOR.validate(object, groups);
        if (constraintViolations.stream().findFirst().isPresent()){
            throw new ValidationException(constraintViolations.stream().findFirst().get().getMessage());
        }
    }
}
