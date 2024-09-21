package com.reyco.dasbx.login.core;

import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
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
		@RibbonClient(value="common-service",configuration=NacosClusterWeightRule.class),
		@RibbonClient(value="open-service",configuration=NacosClusterWeightRule.class),
		@RibbonClient(value="user-service",configuration=NacosClusterWeightRule.class)
})
@EnableCaching
@ComponentScan(value= {"com.reyco.dasbx.login","com.reyco.dasbx.config"})
@MapperScan("com.reyco.dasbx.login.core.dao")
@SpringBootApplication(exclude=DataSourceAutoConfiguration.class)
public class LoginDasbxApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(LoginDasbxApplication.class, args);
	}
	@Bean
	public WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
		return (factory) -> factory.addContextCustomizers(
				(context) -> context.setCookieProcessor(new LegacyCookieProcessor()));
	}
}
