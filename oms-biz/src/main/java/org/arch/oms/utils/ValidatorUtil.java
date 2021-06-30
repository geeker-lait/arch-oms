package org.arch.oms.utils;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.arch.framework.beans.exception.BusinessException;
import org.arch.oms.common.ExceptionStatusCode;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * 校验工具类
 */
public class ValidatorUtil {

    public static <T> void validate(Validator validator, T t, Class clazz) {
        Set<ConstraintViolation<T>> setValidate = validator.validate(t, clazz);
        if (CollectionUtils.isNotEmpty(setValidate)) {
            throw new BusinessException(ExceptionStatusCode.getDefaultExceptionCode(setValidate.iterator().next().getMessage()));

        }
    }

    /**
     * 字符串空值判断
     * @param str
     * @param errorMessage
     */
    public static String checkBlank(String str, String errorMessage) {
        if (StringUtils.isBlank(str)) {
            throw new BusinessException(ExceptionStatusCode.getDefaultExceptionCode(errorMessage));
        }
        return str.trim();
    }

    /**
     * 为空 判断
     * @param obj
     * @param errorMessage
     */
    public static void checkNull(Object obj, String errorMessage) {
        if (ObjectUtils.isEmpty(obj)) {
            throw new BusinessException(ExceptionStatusCode.getDefaultExceptionCode(errorMessage));
        }
    }

    /**
     * 不为空 判断
     * @param obj
     * @param errorMessage
     */
    public static void checkNotNull(Object obj, String errorMessage) {
        if (obj != null) {
            throw new BusinessException(ExceptionStatusCode.getDefaultExceptionCode(errorMessage));
        }
    }
}
