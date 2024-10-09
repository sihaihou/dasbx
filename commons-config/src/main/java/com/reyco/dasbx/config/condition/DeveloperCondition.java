package com.reyco.dasbx.config.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class DeveloperCondition implements Condition{
	
	private static final String ACTIVE_PROFILE = "dev";
	
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		String[] activeProfiles = context.getEnvironment().getActiveProfiles();
		for(String activeProfile : activeProfiles) {
			if(activeProfile.equalsIgnoreCase(ACTIVE_PROFILE)) {
				return true;
			}
		}
		return false;
	}

}
