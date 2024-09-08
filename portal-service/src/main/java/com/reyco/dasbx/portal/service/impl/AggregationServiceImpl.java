package com.reyco.dasbx.portal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.model.domain.SysVip;
import com.reyco.dasbx.portal.model.domain.vo.CategoryListVO;
import com.reyco.dasbx.portal.model.domain.vo.CountryListVO;
import com.reyco.dasbx.portal.model.domain.vo.TypeListVO;
import com.reyco.dasbx.portal.model.domain.vo.YearListVO;
import com.reyco.dasbx.portal.service.AggregationService;
import com.reyco.dasbx.portal.service.CategoryService;
import com.reyco.dasbx.portal.service.CountryService;
import com.reyco.dasbx.portal.service.SysVipService;
import com.reyco.dasbx.portal.service.TypeService;
import com.reyco.dasbx.portal.service.YearService;

@Service
public class AggregationServiceImpl implements AggregationService {
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CountryService countryService;
	@Autowired
	private TypeService typeService;
	@Autowired
	private YearService yearService;
	@Autowired
	private SysVipService sysVipService;
	@Override
	public List<Aggregations> listCategory(Integer size) {
		List<CategoryListVO> categoryListVOs = categoryService.listByLimit(size);
		return categoryListVOs.stream().map(categoryListVO->{
			Aggregations aggregation = new Aggregations();
			aggregation.setId(categoryListVO.getId());
			aggregation.setValue(categoryListVO.getName());
			aggregation.setType((byte)0);
			return aggregation;
		}).collect(Collectors.toList());
	}
	
	@Override
	public List<List<Aggregations>> listAggregationsByCategoryId(Long categoryId) throws Exception {
		Callable<List<Aggregations>> countryTask= new Callable<List<Aggregations>>() {
			@Override
			public List<Aggregations> call() throws Exception {
				List<CountryListVO> countryListVOs = countryService.listByCategoryId(categoryId);
				CountryListVO country = new CountryListVO();
				country.setId(-1L);
				country.setName("全部地区");
				countryListVOs.add(0, country);
				return countryListVOs.stream().map(countryListVO->{
					Aggregations aggregation = new Aggregations();
					aggregation.setId(countryListVO.getId());
					aggregation.setValue(countryListVO.getName());
					aggregation.setType((byte)1);
					return aggregation;
				}).collect(Collectors.toList());
			}
		};
		Callable<List<Aggregations>> typeTask= new Callable<List<Aggregations>>() {
			@Override
			public List<Aggregations> call() throws Exception {
				List<TypeListVO> typeListVOs = typeService.listByCategoryId(categoryId);
				TypeListVO type = new TypeListVO();
				type.setId(-1L);
				type.setName("全部类型");
				typeListVOs.add(0, type);
				return typeListVOs.stream().map(typeListVO->{
					Aggregations aggregation = new Aggregations();
					aggregation.setId(typeListVO.getId());
					aggregation.setValue(typeListVO.getName());
					aggregation.setType((byte)2);
					return aggregation;
				}).collect(Collectors.toList());
			}
		};
		Callable<List<Aggregations>> yearTask= new Callable<List<Aggregations>>() {
			@Override
			public List<Aggregations> call() throws Exception {
				List<YearListVO> yearListVOs = yearService.listByCategoryId(categoryId);
				YearListVO year = new YearListVO();
				year.setId(-1L);
				year.setName("全部年份");
				yearListVOs.add(0, year);
				return yearListVOs.stream().map(yearListVO->{
					Aggregations aggregation = new Aggregations();
					aggregation.setId(yearListVO.getId());
					aggregation.setValue(yearListVO.getName());
					aggregation.setType((byte)3);
					return aggregation;
				}).collect(Collectors.toList());
			}
		};
		FutureTask<List<Aggregations>> countryFutureTask = new FutureTask<>(countryTask);
		new Thread(countryFutureTask).start();
		FutureTask<List<Aggregations>> typeFutureTask = new FutureTask<>(typeTask);
		new Thread(typeFutureTask).start();
		FutureTask<List<Aggregations>> yearFutureTask = new FutureTask<>(yearTask);
		new Thread(yearFutureTask).start();
		List<Aggregations> countryAggregations = countryFutureTask.get();
		List<Aggregations> typeAggregations = typeFutureTask.get();
		List<Aggregations> yearAggregations = yearFutureTask.get();
		List<Aggregations> sortAggregations = getSortAggregations();
		List<List<Aggregations>> aggregations =  new ArrayList<List<Aggregations>>();
		aggregations.add(countryAggregations);
		aggregations.add(typeAggregations);
		aggregations.add(yearAggregations);
		aggregations.add(sortAggregations);
		return aggregations;
	}
	private List<Aggregations> getSortAggregations(){
		List<Aggregations> sortAggregations = new ArrayList<>();
		Aggregations comprehensiveAggregation = new Aggregations();
		comprehensiveAggregation.setId(-1L);
		comprehensiveAggregation.setValue("综合排序");
		comprehensiveAggregation.setType((byte)4);
		Aggregations hotAggregation = new Aggregations();
		hotAggregation.setId(1L);
		hotAggregation.setValue("热度最高");
		hotAggregation.setType((byte)4);
		Aggregations mostAggregation = new Aggregations();
		mostAggregation.setId(2L);
		mostAggregation.setValue("最多播放");
		mostAggregation.setType((byte)4);
		sortAggregations.add(comprehensiveAggregation);
		sortAggregations.add(hotAggregation);
		sortAggregations.add(mostAggregation);
		return sortAggregations;
	}
	
	@Override
	public Map<String, List<Aggregations>> listAggregationMapByCategoryId(Long categoryId) throws Exception {
		Callable<List<Aggregations>> countryTask= new Callable<List<Aggregations>>() {
			@Override
			public List<Aggregations> call() throws Exception {
				List<CountryListVO> countryListVOs = countryService.listByCategoryId(categoryId);
				CountryListVO country = new CountryListVO();
				country.setId(-1L);
				country.setName("请选择");
				countryListVOs.add(0, country);
				return countryListVOs.stream().map(countryListVO->{
					Aggregations aggregation = new Aggregations();
					aggregation.setId(countryListVO.getId());
					aggregation.setValue(countryListVO.getName());
					aggregation.setType((byte)1);
					return aggregation;
				}).collect(Collectors.toList());
			}
		};
		Callable<List<Aggregations>> typeTask= new Callable<List<Aggregations>>() {
			@Override
			public List<Aggregations> call() throws Exception {
				List<TypeListVO> typeListVOs = typeService.listByCategoryId(categoryId);
				TypeListVO type = new TypeListVO();
				type.setId(-1L);
				type.setName("请选择");
				typeListVOs.add(0, type);
				return typeListVOs.stream().map(typeListVO->{
					Aggregations aggregation = new Aggregations();
					aggregation.setId(typeListVO.getId());
					aggregation.setValue(typeListVO.getName());
					aggregation.setType((byte)2);
					return aggregation;
				}).collect(Collectors.toList());
			}
		};
		Callable<List<Aggregations>> yearTask= new Callable<List<Aggregations>>() {
			@Override
			public List<Aggregations> call() throws Exception {
				List<YearListVO> yearListVOs = yearService.listByCategoryId(categoryId);
				YearListVO year = new YearListVO();
				year.setId(-1L);
				year.setName("请选择");
				yearListVOs.add(0, year);
				return yearListVOs.stream().map(yearListVO->{
					Aggregations aggregation = new Aggregations();
					aggregation.setId(yearListVO.getId());
					aggregation.setValue(yearListVO.getName());
					aggregation.setType((byte)3);
					return aggregation;
				}).collect(Collectors.toList());
			}
		};
		Callable<List<Aggregations>> vipTask= new Callable<List<Aggregations>>() {
			@Override
			public List<Aggregations> call() throws Exception {
				List<SysVip> sysVips = sysVipService.list();
				return sysVips.stream().map(sysVip->{
					Aggregations aggregation = new Aggregations();
					aggregation.setId(sysVip.getId());
					aggregation.setValue(sysVip.getName());
					aggregation.setType((byte)10);
					return aggregation;
				}).collect(Collectors.toList());
			}
		};
		FutureTask<List<Aggregations>> countryFutureTask = new FutureTask<>(countryTask);
		new Thread(countryFutureTask).start();
		FutureTask<List<Aggregations>> typeFutureTask = new FutureTask<>(typeTask);
		new Thread(typeFutureTask).start();
		FutureTask<List<Aggregations>> yearFutureTask = new FutureTask<>(yearTask);
		new Thread(yearFutureTask).start();
		FutureTask<List<Aggregations>> vipFutureTask = new FutureTask<>(vipTask);
		new Thread(vipFutureTask).start();
		List<Aggregations> countryAggregations = countryFutureTask.get();
		List<Aggregations> typeAggregations = typeFutureTask.get();
		List<Aggregations> yearAggregations = yearFutureTask.get();
		List<Aggregations> vipAggregations = vipFutureTask.get();
		Map<String, List<Aggregations>> resultMap = new HashMap<String, List<Aggregations>>();
		resultMap.put("countrys", countryAggregations);
		resultMap.put("types", typeAggregations);
		resultMap.put("years", yearAggregations);
		resultMap.put("vips", vipAggregations);
		return resultMap;
	}
	
}
