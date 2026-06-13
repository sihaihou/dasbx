package com.reyco.dasbx.es.support.annotation.sort;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分页查询注解:必须使用 {@code com.reyco.dasbx.es.support.annotation.sort.EsSortParam}对象.
 * <p>使用示例：</p>
 * <pre>{@code
 * @EsSort
 * private EsSortParam sort;
 * </pre>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EsSort {
    
    String field() default "";
    
    
}