package com.reyco.dasbx.resource.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AliasFor;

import com.reyco.dasbx.resource.constant.ResourceMode;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ResourceConfigurationSelector.class)
public @interface EnableResource {
	
	ResourceMode resourceMode() default ResourceMode.ANNOTATION;
	/**
	 * @see execution(public * com..controller..*.*(..))
	 * @return
	 */
	@AliasFor("expression")
	String value() default "";
	/**
	 * @see execution(public * com..controller..*.*(..))
	 * @return
	 */
	@AliasFor("value")
	String expression() default "";
	
	/**
	 * @see com.*.controller.*
	 * @return
	 */
	String[] patterns() default {};
	/**
	 * @see com.*.controller.*.TestController.*
	 * @return
	 */
	String[] excludedPatterns() default {};
	
	String[] tokenNames() default {"authentication","token"};
	
	int order() default Ordered.HIGHEST_PRECEDENCE;
}
