package com.reyco.dasbx.gateway.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.dasbx.gateway.core.service.DynamicRouteService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("route")
public class DynamicRouteController {
	@Autowired
	DynamicRouteService dynamicRouteService;
	
	@PostMapping
	public Mono<String> save(@RequestBody RouteDefinition routeDefinition){
		return dynamicRouteService.save(routeDefinition);
	}
	@DeleteMapping("/delete/{routeId}")
	public Mono<String> delete(@PathVariable String routeId){
		return dynamicRouteService.delete(routeId);
	}
	@PutMapping
	public Mono<String> update(@RequestBody RouteDefinition routeDefinition){
		return dynamicRouteService.update(routeDefinition);
	}
}
