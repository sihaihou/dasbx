package com.reyco.dasbx.gateway.core.irule;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.ExtendBalancer;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;

public class NacosClusterWeightRule extends AbstractLoadBalancerRule{
	private static Logger logger = LoggerFactory.getLogger(NacosClusterWeightRule.class);
	@Autowired
	private NacosDiscoveryProperties nacosDiscoveryProperties;
	
	@Override
	public Server choose(Object key) {
		BaseLoadBalancer loadBalancer = (BaseLoadBalancer)super.getLoadBalancer();
		NamingService namingServiceInstance = nacosDiscoveryProperties.namingServiceInstance();
		try {
			List<Instance> selectInstances = namingServiceInstance.selectInstances(loadBalancer.getName(),nacosDiscoveryProperties.getGroup() , true);
			List<Instance> instances = selectInstances.stream().filter( instance -> instance.getClusterName().equals(nacosDiscoveryProperties.getClusterName())).collect(Collectors.toList());
			if(instances==null || instances.isEmpty()) {
				instances = selectInstances;
			}
			Instance instance = ExtendBalancer.getHostByRandomWeight2(instances);
			return new NacosServer(instance);
		} catch (NacosException e) {
			logger.error("根据权重获取服务失败：异常未e:"+e);
		}
		return null;
	}

	@Override
	public void initWithNiwsConfig(IClientConfig clientConfig) {
		
	}
}
