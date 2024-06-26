package com.reyco.dasbx.resource.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Resource {
	
	@AliasFor("name")
	String value() default "";
	
	@AliasFor("value")
	String name() default "";
	/**
	 * 资源消息
	 * @return
	 */
	String resource() default "";
}
