package com.reyco.dasbx.es.support.annotation.query;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EsQuery {
	
	/**
     * 查询类型
     */
    EsQueryType type() default EsQueryType.TERM;
	/**
     * 对应 ES 中的字段名。不填默认取 Java 属性名
     */
    String field() default "";
    /**
     * 专用于 MULTI_MATCH：指定要同时检索的多个 ES 字段名
     */
    String[] multiFields() default {};
    
}
