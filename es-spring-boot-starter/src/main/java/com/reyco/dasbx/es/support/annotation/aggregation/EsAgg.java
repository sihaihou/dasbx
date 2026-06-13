package com.reyco.dasbx.es.support.annotation.aggregation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.reyco.dasbx.es.support.result.facet.DefaultLabelProvider;
import com.reyco.dasbx.es.support.result.facet.FacetLabelProvider;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EsAgg {
	
	String name() default "";
	
	String field() default "";
	
	EsAggType type() default EsAggType.TERMS;
	
	boolean order() default true;
	
	int size() default 10;
	
	Class<? extends FacetLabelProvider> provider() default DefaultLabelProvider.class;
	
}
