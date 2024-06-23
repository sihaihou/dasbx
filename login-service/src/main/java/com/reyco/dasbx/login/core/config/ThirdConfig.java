package com.reyco.dasbx.login.core.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@ConfigurationProperties(prefix=ThirdConfig.THIRD_PREFIX)
public class ThirdConfig {
	
	public final static String THIRD_PREFIX = "third";
	
	private Map<String,Party> party;

	public Map<String, Party> getParty() {
		return party;
	}

	public void setParty(Map<String, Party> party) {
		this.party = party;
	}

}	
