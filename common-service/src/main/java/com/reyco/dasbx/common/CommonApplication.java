package com.reyco.dasbx.common;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import com.reyco.dasbx.resource.annotation.EnableResource;
import com.reyco.dasbx.resource.constant.ResourceMode;

@EnableResource(
		resourceMode=ResourceMode.REGULAR,
		patterns= {"com.*.controller.*"},
		excludedPatterns= {
				"com.*.controller.*.SysLogController.*",
				"com.*.controller.*.TestController.*"
				},
		tokenNames= {"dasbx-token","dasbx-code"})
@EnableDiscoveryClient
@EnableFeignClients
@EnableCaching
@ComponentScan(value= {"com.reyco.dasbx.common","com.reyco.dasbx.config"})
@MapperScan("com.reyco.dasbx.common.core.dao")
@SpringBootApplication(exclude=DataSourceAutoConfiguration.class)
public class CommonApplication {
	public static void main(String[] args) {
		SpringApplication.run(CommonApplication.class, args);
	}
}
