package com.reyco.dasbx.es.support.result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.reyco.dasbx.commons.utils.Page;
import com.reyco.dasbx.es.core.result.SearchResult;
import com.reyco.dasbx.es.core.result.page.OffsetPageResult;
import com.reyco.dasbx.es.core.result.page.ScrollPageResult;
import com.reyco.dasbx.es.core.result.page.SearchAfterPageResult;
import com.reyco.dasbx.es.support.result.facet.FacetConverter;
import com.reyco.dasbx.es.support.result.facet.FacetDefinition;
import com.reyco.dasbx.es.support.result.facet.FacetLabelProvider;
import com.reyco.dasbx.es.support.result.facet.FacetResult;
import com.reyco.dasbx.es.support.result.record.RecordProcessor;

public class ResultProcessor {

	private RecordProcessor recordProcessor;
	
	private FacetConverter facetConverter;
	
	public ResultProcessor(RecordProcessor recordProcessor, FacetConverter facetConverter) {
		super();
		this.recordProcessor = recordProcessor;
		this.facetConverter = facetConverter;
	}

	public <T> Result<T> process(SearchResult<T> search, List<FacetDefinition> defs) {
		Map<String,Class<? extends FacetLabelProvider>> providerMap = new HashMap<>();
		defs.stream().forEach(facetDefinition->{
			String field = facetDefinition.getField();
			String name = StringUtils.hasText(field) ? field : facetDefinition.getName();
			providerMap.put(name, facetDefinition.getProviderClass());
		});
		return process(search, providerMap);
	}
	
	public <T> Result<T> process(SearchResult<T> search, Map<String,Class<? extends FacetLabelProvider>> providerMap) {
		Result<T> result = new Result<>();
        // 提前初始化 Page，确保即使 search 为 null，返回的 Result 结构也是完整的（包含空 Page）
		Page<T> page = new Page<T>();
		result.setPage(page);
		
		if (search == null) {
            return result;
        }
		// 1.处理记录
		List<T> convertedRecords = recordProcessor.process(search.getRecords());
		page.setList(convertedRecords);
		
		// 2. 处理分页
		if(search.getPage() instanceof OffsetPageResult) {
			OffsetPageResult pageResult = (OffsetPageResult) search.getPage();
			page.setPageNum(pageResult.getPageNum());
			page.setPageSize(pageResult.getPageSize());
			if (pageResult.getTotal() != null) {
                page.setTotal(pageResult.getTotal().intValue());
            }
		}else if(search.getPage() instanceof SearchAfterPageResult) {
			SearchAfterPageResult pageResult = (SearchAfterPageResult) search.getPage();
			page.setPageSize(pageResult.getPageSize());
			page.setNextSearchAfter(pageResult.getNextSearchAfter());
		}else if(search.getPage() instanceof ScrollPageResult) {
			ScrollPageResult pageResult = (ScrollPageResult) search.getPage();
			page.setPageSize(pageResult.getPageSize());
			page.setScrollId(pageResult.getScrollId());
		}
		
		// 3.处理聚合
		List<FacetResult> facetResults = facetConverter.convert(search.getAggregations(), providerMap);
		result.setAggregations(facetResults);
		
		return result;
	}
	
}
