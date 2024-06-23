package com.reyco.dasbx.gateway.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.gateway.core.filter.AuthGlobalFilter;

import reactor.core.publisher.Mono;

@Service
public class DynamicRouteService implements ApplicationContextAware {
	private static final Logger logger= LoggerFactory.getLogger(AuthGlobalFilter.class);
	@Autowired
	RouteDefinitionWriter routeDefinitionWriter;
	
	ApplicationContext applicationContext;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	public Mono<String> save(RouteDefinition definition) {
		return routeDefinitionWriter.save(Mono.just(definition)).then(Mono.defer(()->{
			applicationContext.publishEvent(new RefreshRoutesEvent(this));
			logger.info("添加路由成功，路由Id："+definition.getId());
			return Mono.just("ok");
		}).onErrorResume(e->{
			return e instanceof Exception;
		},r->{
			logger.error("添加路由失败，路由Id："+definition.getId());
			return Mono.just("fail");
		}));
	}
	public Mono<String> delete(String routeId) {
		return routeDefinitionWriter.delete(Mono.just(routeId)).then(Mono.defer(()->{
			logger.info("删除路由成功，路由Id："+routeId);
			applicationContext.publishEvent(new RefreshRoutesEvent(this));
			return Mono.just("ok");
		}).onErrorResume(e->{
			return e instanceof NotFoundException;
		},r->{
			logger.error("删除路由失败，路由Id："+routeId);
			return Mono.just("fail");
		}));
	}
	public Mono<String> update(RouteDefinition definition) {
		delete(definition.getId());
		return save(definition);
	}
}
