package com.reyco.dasbx.config.mysql;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MySQLConfig {
	
	@RefreshScope
	@ConditionalOnMissingBean
	@Bean(initMethod="init")
	public DataSource dataSource() {
		return new DruidDataSourceWrapper();
	}
}
