package com.reyco.dasbx.gateway.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;

import com.reyco.dasbx.gateway.core.irule.NacosClusterWeightRule;

/*@RibbonClients(value= {
		@RibbonClient(value="user-service",configuration=NacosClusterWeightRule.class),
		@RibbonClient(value="login-service",configuration=NacosClusterWeightRule.class),
		@RibbonClient(value="open-service",configuration=NacosClusterWeightRule.class),
		@RibbonClient(value="portal-service",configuration=NacosClusterWeightRule.class),
		@RibbonClient(value="common-service",configuration=NacosClusterWeightRule.class)
})*/
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayDasbxApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(GatewayDasbxApplication.class, args);
	}
}
