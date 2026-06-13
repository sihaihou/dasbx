package com.reyco.dasbx.es.support.result.record;

import java.util.ArrayList;
import java.util.List;

import com.reyco.dasbx.es.core.result.SearchHitResult;

public class RecordProcessor {
	
	private SearchHitProcessorRegistry searchHitProcessorRegistry;
	
	public RecordProcessor(SearchHitProcessorRegistry searchHitProcessorRegistry) {
		super();
		this.searchHitProcessorRegistry = searchHitProcessorRegistry;
	}

	public <T> List<T> process(List<SearchHitResult<T>> records){
		if (records == null || records.isEmpty()) {
            return new ArrayList<>(0);
        }
		// 1. 初始化时指定容量，完美避免 ArrayList 内部数组扩容的性能损耗
        List<T> ts = new ArrayList<>(records.size());
        records.stream().forEach(searchHitResult->{
        	T t = searchHitResult.getSource();
        	//
			searchHitProcessorRegistry.process(t, searchHitResult);
			//
			ts.add(t);
        });
		return ts;
	}
	
}
