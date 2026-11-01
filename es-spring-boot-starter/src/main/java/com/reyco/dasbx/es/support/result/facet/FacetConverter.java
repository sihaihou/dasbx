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
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.util.StringUtils;

import com.reyco.dasbx.es.core.feature.aggregation.AggregationNode;

public class FacetConverter implements BeanFactoryAware {

	private final Map<Class<? extends FacetLabelProvider>, FacetLabelProvider> providersCache = new ConcurrentHashMap<>();

	private BeanFactory beanFactory;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
	
	public List<FacetResult> convert(List<AggregationNode> aggs, List<FacetDefinition> defs) {
		if (aggs == null || aggs.isEmpty() || defs == null || defs.isEmpty()) {
			return new ArrayList<FacetResult>(0); // 避免创建无用对象
		}
		return convert(aggs, resolveProviders(defs));
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
		return providersCache.computeIfAbsent(providerClass, this::createProvider);
	}

	@SuppressWarnings("unchecked")
	private <T extends FacetLabelProvider> T createProvider(Class<T> providerClass) {
		if (beanFactory instanceof ListableBeanFactory) {
	        ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;

	        Map<String, T> beans = listableBeanFactory.getBeansOfType(providerClass);

	        if (!beans.isEmpty()) {
	            return beans.values().iterator().next();
	        }
	    }
		if (beanFactory instanceof AutowireCapableBeanFactory) {
			AutowireCapableBeanFactory autowireFactory = (AutowireCapableBeanFactory) beanFactory;
			return (T) autowireFactory.createBean(providerClass, AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR, true);
		}
		return BeanUtils.instantiateClass(providerClass);
	}
	
	
	
	@SuppressWarnings("unused")
	public Map<String, Class<? extends FacetLabelProvider>> resolveProviders(List<FacetDefinition> defs) {
		if (defs == null) {
			return new HashMap<>();
		}
		// 2. 根据 defs 大小初始化 Map，防止扩容开销 (公式: size / 0.75 + 1)
		int initialCapacity = (int) (defs.size() / 0.75F + 1.0F);
		Map<String, Class<? extends FacetLabelProvider>> providerMap = new HashMap<>(initialCapacity);
	    for (FacetDefinition def : defs) {
	        if (def == null) {
	            throw new FacetException(
	                    "Facet definition must not be null");
	        }
	        String name = def.getName();
	        String key = StringUtils.hasText(name) ? name : def.getField();
	        if (!StringUtils.hasText(key)) {
	            throw new FacetException(
	                    "Facet name or field must not be empty");
	        }
	        if (def.getProviderClass() == null) {
	            throw new FacetException(
	                    "Facet provider must not be null: " + key);
	        }
	        if (providerMap.containsKey(key)) {
	            throw new FacetException(
	                    "Duplicate facet name: " + key);
	        }
	        providerMap.put(key, def.getProviderClass());
	    }
	    return providerMap;
	}
}
