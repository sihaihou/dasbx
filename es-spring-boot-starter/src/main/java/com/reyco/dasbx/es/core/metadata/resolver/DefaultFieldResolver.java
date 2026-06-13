package com.reyco.dasbx.es.core.metadata.resolver;

import com.reyco.dasbx.es.core.metadata.FieldCapability;
import com.reyco.dasbx.es.core.metadata.FieldMetadata;
import com.reyco.dasbx.es.core.metadata.register.MetadataRegistry;

public class DefaultFieldResolver implements FieldResolver {

	private MetadataRegistry registry;

	public DefaultFieldResolver(MetadataRegistry registry) {
		this.registry = registry;
	}

	@Override
	public String resolve(Class<?> entityClass, String property, FieldCapability capability) {
		FieldMetadata metadata = registry.getField(entityClass, property);
		if(metadata==null) {
			return property;
		}
		return metadata.resolveField(capability);
	}

}
