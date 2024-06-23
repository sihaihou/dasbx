package com.reyco.dasbx.user.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties()
public class ESSearchIndexProperties {
	
	public final static String ELSTICSEARCH_INDEX_PREFIX = "reyco.dasbx.elasticsearch";
	
	
}
