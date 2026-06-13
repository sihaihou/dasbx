package com.reyco.dasbx.rate.limit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import org.springframework.core.annotation.AliasFor;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SlidingWindowRateLimit {
	/**
     * 限流名称
     */
    @AliasFor(value = "name")
    String value() default "default";
    
    @AliasFor(value = "value")
    String name() default "default";
    /**
     * 限流维度（支持 SpEL 表达式）
     */
    String key() default "";
    /**
     * 前缀
     */
    String prefix() default "rate:limit:sliding:window:";
    /**
     * 时间窗口大小（秒）
     */
    int window() default 1;
    /**
	 *  时间窗口单位（支持SECONDS、MINUTES、HOURS 或 DAYS）
	 * @return
	 */
    TimeUnit unit() default TimeUnit.SECONDS;
    /**
     * 窗口内最大请求次数
     */
    int maxRequests() default 100;
    
}
