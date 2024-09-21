package com.reyco.dasbx.lock.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

/**
*@author reyco
*<pre>
*
*<pre> 
*/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Lock {
	/**
	 * 锁的名称
	 * @return
	 */
	@AliasFor(value="name")
	String value() default "default";
	/**
	 * 锁的名称
	 * @return
	 */
	@AliasFor(value="value")
	String name() default "default";
	/**
	 * 锁的名称key
	 * @return
	 */
	String key() default "";
	/**
	 * 锁的名称前缀
	 * @return
	 */
	String prefix() default "distributedLock:";
	/**
	 * 过期时间/毫秒
	 * @return
	 */
	int expireTime() default 3000;
}