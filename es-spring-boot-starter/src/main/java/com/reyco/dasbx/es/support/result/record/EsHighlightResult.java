package com.reyco.dasbx.es.support.result.record;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EsHighlightResult {
    /**
     * 对应 ES 传回来的高亮 key（即原字段名）
     */
    String value() default "";
}