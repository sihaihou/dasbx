package com.reyco.dasbx.resource.annotation;

import org.springframework.context.annotation.AutoProxyRegistrar;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class ResourceConfigurationSelector implements ImportSelector {

	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		return new String[] {
				AutoProxyRegistrar.class.getName(),
				ResourceHandlerConfiguration.class.getName(),
				ResourceConfiguration.class.getName()
			};
	}

}
