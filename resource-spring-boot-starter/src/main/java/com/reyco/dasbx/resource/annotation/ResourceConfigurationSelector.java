package com.reyco.dasbx.resource.annotation;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.context.annotation.AutoProxyRegistrar;

public class ResourceConfigurationSelector extends AdviceModeImportSelector<EnableResource> {

	@Override
	protected String[] selectImports(AdviceMode adviceMode) {
		switch (adviceMode) {
		case PROXY:
			return new String[] {
					AutoProxyRegistrar.class.getName(),
					ResourceHandlerConfiguration.class.getName(),
					ResourceConfiguration.class.getName()
				};
		case ASPECTJ:
			return new String[] {};
		default:
			return null;
		}
	}

}
