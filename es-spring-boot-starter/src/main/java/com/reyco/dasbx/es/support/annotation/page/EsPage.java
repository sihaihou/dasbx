package com.reyco.dasbx.es.support.annotation.page;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分页查询注解:必须使用 {@code com.reyco.dasbx.es.support.annotation.page.EsPageParam}对象.
 * 优先级：代码 > 注解 > 默认值
 * <p>使用示例：</p>
 * <pre>{@code
 * @EsPage
 * private EsPageParam page;
 * </pre>
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EsPage {
	
}
