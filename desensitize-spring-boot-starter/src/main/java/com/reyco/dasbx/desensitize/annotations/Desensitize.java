package com.reyco.dasbx.desensitize.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.reyco.dasbx.desensitize.core.DesensitizeType;
import com.reyco.dasbx.desensitize.core.ReycoDesensitizeSerializer;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = ReycoDesensitizeSerializer.class)
public @interface Desensitize {
	
	/**
	 * 脱敏类型
	 * @return
	 */
	Class<? extends DesensitizeType> type() default DesensitizeType.DefaultAllExpressionDesensitizeType.class;
	
	/**
     * 脱敏起始位置
     */
    int start() default Integer.MIN_VALUE;

    /**
     * 脱敏结束位置
     */
    int end() default Integer.MAX_VALUE;
}
