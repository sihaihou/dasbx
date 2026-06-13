package com.reyco.dasbx.es.support.annotation.source;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.reyco.dasbx.es.support.annotation.SourceType;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EsSource {

    SourceType value() default SourceType.INCLUDE;

}