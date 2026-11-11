package com.reyco.dasbx.desensitize.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.reyco.dasbx.desensitize.DesensitizeType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Desensitize {
	
	/**
	 * 脱敏类型
	 * @return
	 */
	Class<? extends DesensitizeType> type() default DesensitizeType.DefaultAllExpressionDesensitizeType.class;
	
	/**
     * 保留的前缀字符数
     */
    int start() default Integer.MIN_VALUE;

    /**
     * 保留的后缀字符数
     */
    int end() default Integer.MAX_VALUE;
}
