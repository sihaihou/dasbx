package com.reyco.dasbx.rate.limit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import org.springframework.core.annotation.AliasFor;

/**
 * 令牌桶限流注解
 * @author reyco
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenBucketRateLimit {
	/**
	 * 限流地址
	 * @return
	 */
	@AliasFor(value="name")
	String value() default "default";
	
	@AliasFor(value="value")
	String name() default "default";
	 /**
     * 限流维度（支持 SpEL 表达式）
     * @return
     */
    String key() default "";
    /**
	 * 前缀
	 * @return
	 */
	String prefix() default "rate:limit:token:bucket:";
	/**
	 * 令牌生成周期
	 * @return
	 */
	int period() default 1;
	/**
	 * 令牌生成周期单位
	 * @return
	 */
    TimeUnit unit() default TimeUnit.SECONDS;
    
    /**
     * 每周期生成的令牌数
     * @return
     */
    int rate() default 10;
    /**
     * 最大容量
     * @return
     */
    int capacity() default 100;
    
}
