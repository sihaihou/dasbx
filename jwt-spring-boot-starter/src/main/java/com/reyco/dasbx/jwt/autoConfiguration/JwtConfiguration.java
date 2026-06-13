package com.reyco.dasbx.jwt.autoConfiguration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.dasbx.jwt.core.JwtUtils;
import com.reyco.dasbx.jwt.properties.JwtProperties;

import io.jsonwebtoken.Jwts;

@Configuration
@ConditionalOnClass(Jwts.class)
@ConditionalOnProperty(name="reyco.dasbx.jwt.enabled",matchIfMissing=true)
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(JwtUtils.class)
	public JwtUtils jwtUtils(JwtProperties JwtProperties) {
		JwtUtils jwtUtils = new JwtUtils(JwtProperties);
		return jwtUtils;
	}
	
}
