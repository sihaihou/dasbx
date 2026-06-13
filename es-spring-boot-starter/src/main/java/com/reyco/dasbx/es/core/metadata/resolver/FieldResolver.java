package com.reyco.dasbx.es.core.metadata.resolver;

import com.reyco.dasbx.es.core.metadata.FieldCapability;

public interface FieldResolver {

	String resolve(Class<?> entityClass, String property, FieldCapability capability);

}
