package com.reyco.dasbx.es.support.result.facet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import com.reyco.dasbx.es.core.feature.aggregation.AggregationNode;

public class FacetConverter implements BeanFactoryAware {

	public final static Map<Class<? extends FacetLabelProvider>, FacetLabelProvider> PROVIDERS_CACHE = new ConcurrentHashMap<>();

	private BeanFactory beanFactory;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public List<FacetResult> convert(List<AggregationNode> aggs, List<FacetDefinition> defs) {
		if (aggs == null || aggs.isEmpty() || defs == null || defs.isEmpty()) {
			return new ArrayList<FacetResult>(0); // 避免创建无用对象
		}
		// 2. 根据 defs 大小初始化 Map，防止扩容开销 (公式: size / 0.75 + 1)
		int initialCapacity = (int) (defs.size() / 0.75F + 1.0F);
		Map<String, Class<? extends FacetLabelProvider>> providerMap = new HashMap<>(initialCapacity);
		for (FacetDefinition def : defs) {
			providerMap.put(def.getName(), def.getProviderClass());
		}
		return convert(aggs, providerMap);
	}

	public List<FacetResult> convert(List<AggregationNode> aggs,Map<String, Class<? extends FacetLabelProvider>> providerMap) {
		if (aggs == null || aggs.isEmpty() || providerMap == null || providerMap.isEmpty()) {
			return new ArrayList<FacetResult>(0); // 避免创建无用对象
		}

		List<FacetResult> result = new ArrayList<FacetResult>(aggs.size()); // 预设聚合结果集大小

		for (AggregationNode agg : aggs) {
			Class<? extends FacetLabelProvider> providerClazz = providerMap.get(agg.getName());
			if (providerClazz == null) {
				continue;
			}

			FacetResult facet = new FacetResult();
			facet.setName(agg.getName());

			List<AggregationNode> buckets = agg.getChildren();
			if (buckets != null && !buckets.isEmpty()) {
				List<FacetItem> items = new ArrayList<>(buckets.size()); // 预设 bucket 大小

				for (AggregationNode bucket : buckets) {
					FacetItem item = new FacetItem();
					item.setKey(bucket.getKey());
					item.setCount(bucket.getDocCount());

					FacetLabelProvider provider = getProvider(providerClazz);
					item.setLabel(provider.getLabel(bucket.getKey()));
					
					items.add(item);
				}
				facet.setItems(items);
			}
			result.add(facet);
		}

		return result;
	}

	public FacetLabelProvider getProvider(Class<? extends FacetLabelProvider> providerClass) {
		if (providerClass == null) {
			return null;
		}
		return PROVIDERS_CACHE.computeIfAbsent(providerClass, this::createProvider);
	}

	@SuppressWarnings("unchecked")
	private <T extends FacetLabelProvider> T createProvider(Class<T> providerClass) {
		if (beanFactory instanceof AutowireCapableBeanFactory) {
			AutowireCapableBeanFactory autowireFactory = (AutowireCapableBeanFactory) beanFactory;
			return (T) autowireFactory.createBean(providerClass, AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR, true);
		}
		return BeanUtils.instantiateClass(providerClass);
	}
}
