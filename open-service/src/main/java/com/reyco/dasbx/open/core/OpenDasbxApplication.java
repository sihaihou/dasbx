package com.reyco.dasbx.open.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import com.reyco.dasbx.config.irule.NacosClusterWeightRule;
import com.reyco.dasbx.resource.annotation.EnableResource;
import com.reyco.dasbx.resource.constant.ResourceMode;

@EnableResource(
		resourceMode=ResourceMode.REGULAR,
		patterns= {"com.*.controller.*"},
		excludedPatterns= {
				"com.*.controller.*.TestController.*"
				},
		tokenNames= {"dasbx-token","dasbx-code"})
@EnableDiscoveryClient
@EnableFeignClients
@RibbonClients(value= {
		@RibbonClient(value="user-service",configuration=NacosClusterWeightRule.class),
		@RibbonClient(value="common-service",configuration=NacosClusterWeightRule.class)
})
@ComponentScan(value= {"com.reyco.dasbx.open","com.reyco.dasbx.config"})
@MapperScan("com.reyco.dasbx.open.core.dao")
@SpringBootApplication(exclude=DataSourceAutoConfiguration.class)
public class OpenDasbxApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(OpenDasbxApplication.class, args);
	}
	
}
