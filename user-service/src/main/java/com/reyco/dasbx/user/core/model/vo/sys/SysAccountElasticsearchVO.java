package com.reyco.dasbx.user.core.model.vo.sys;

import java.util.List;
import java.util.Map;

import com.reyco.dasbx.commons.utils.Page;
import com.reyco.dasbx.es.core.model.Aggregation;
import com.reyco.dasbx.es.core.search.SearchVO;
import com.reyco.dasbx.user.core.model.vo.SysAccountInfoVO;

public class SysAccountElasticsearchVO implements SearchVO<SysAccountInfoVO> {
	private Map<String,List<Aggregation>> aggregations;
	private Page<SysAccountInfoVO> page;
	@Override
	public Map<String, List<Aggregation>> getAggregations() {
		return aggregations;
	}
	public void setAggregations(Map<String, List<Aggregation>> aggregations) {
		this.aggregations = aggregations;
	}
	@Override
	public Page<SysAccountInfoVO> getPage() {
		return page;
	}
	public void setPage(Page<SysAccountInfoVO> page) {
		this.page = page;
	}
}
