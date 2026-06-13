package com.reyco.dasbx.es.support.annotation.highlight;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EsHighlightField {
	
    /** ES 中实际高亮的字段，不填默认当前属性名 */
    String field() default "";
    
    boolean requireFieldMatch() default false;
    
    /** 前置标签 */
    String preTags() default "<em>";
    
    /** 后置标签 */
    String postTags() default "</em>";
    
}