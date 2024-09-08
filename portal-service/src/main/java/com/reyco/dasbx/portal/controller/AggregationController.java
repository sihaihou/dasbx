package com.reyco.dasbx.portal.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.dasbx.commons.domain.R;
import com.reyco.dasbx.portal.service.AggregationService;
import com.reyco.dasbx.portal.service.AggregationService.Aggregations;

@RestController
@RequestMapping("aggregation")
public class AggregationController {
	@Autowired
	private AggregationService aggregationService;
	
	@GetMapping("listCategory")
	public Object listCategory(int size) {
		List<Aggregations> aggregations = aggregationService.listCategory(size);
		return R.success(aggregations);
	}
	
	@GetMapping("listByCategoryId")
	public Object listByCategoryId(Long categoryId) throws Exception {
		List<List<Aggregations>> aggregations = aggregationService.listAggregationsByCategoryId(categoryId);
		return R.success(aggregations);
	}
	@GetMapping("listAggregationMapByCategoryId")
	public Object listAggregationMapByCategoryId(Long categoryId) throws Exception {
		Map<String, List<Aggregations>> aggregations = aggregationService.listAggregationMapByCategoryId(categoryId);
		return R.success(aggregations);
	}
}
