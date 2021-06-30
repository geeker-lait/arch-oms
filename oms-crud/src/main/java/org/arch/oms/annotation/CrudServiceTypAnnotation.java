package org.arch.oms.annotation;

import org.arch.oms.common.enums.CrudServiceTyp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 批量操作数据库  只支持普通对象类型和 list
 * @author junboXiang
 * @version V1.0
 * 2021-06-29
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CrudServiceTypAnnotation {
    CrudServiceTyp type();
}
