package com.reyco.dasbx.config.irule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;

/** 
 * 权重
 * @author  reyco
 * @date    2021.12.14
 * @version v1.0.1 
 */
public class NacosWeightRule extends AbstractLoadBalancerRule{
	
	private static Logger logger = LoggerFactory.getLogger(NacosWeightRule.class);
	@Autowired
	private NacosDiscoveryProperties nacosDiscoveryProperties;
	@Override
	public Server choose(Object key) {
		BaseLoadBalancer loadBalancer = (BaseLoadBalancer)super.getLoadBalancer();
		NamingService namingServiceInstance = nacosDiscoveryProperties.namingServiceInstance();
		try {
			Instance instance = namingServiceInstance.selectOneHealthyInstance(loadBalancer.getName(), nacosDiscoveryProperties.getGroup());
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
