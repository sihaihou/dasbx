package com.reyco.dasbx.common.core.model.vo.personage;

import java.util.List;
import java.util.Map;

import com.reyco.dasbx.commons.utils.Page;
import com.reyco.dasbx.es.core.model.Aggregation;
import com.reyco.dasbx.es.core.search.ElasticsearchVO;

public class PersonalElasticsearchVO implements ElasticsearchVO<PersonageInfoVO> {
	private Map<String,List<Aggregation>> aggregations;
	private Page<PersonageInfoVO> page;
	@Override
	public Map<String, List<Aggregation>> getAggregations() {
		return aggregations;
	}
	public void setAggregations(Map<String, List<Aggregation>> aggregations) {
		this.aggregations = aggregations;
	}
	@Override
	public Page<PersonageInfoVO> getPage() {
		return page;
	}
	public void setPage(Page<PersonageInfoVO> page) {
		this.page = page;
	}
}
