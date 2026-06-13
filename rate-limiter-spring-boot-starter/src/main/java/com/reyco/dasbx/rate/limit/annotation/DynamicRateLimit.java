package com.reyco.dasbx.rate.limit.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicRateLimit {
    /**
     * 限流名称(用于获取限流配置)
     */
    @AliasFor("resources")
    String value() default "default";
    
    @AliasFor("value")
    String resources() default "default";
    /**
     * 限流key（支持SpEL表达式）
     */
    String key() default "";
    /**
     * 描述
     * @return
     */
    String description() default "";
}
