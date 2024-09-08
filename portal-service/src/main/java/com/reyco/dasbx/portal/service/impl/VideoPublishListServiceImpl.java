package com.reyco.dasbx.portal.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.model.domain.SysVip;
import com.reyco.dasbx.portal.model.domain.vo.CategoryListVO;
import com.reyco.dasbx.portal.model.domain.vo.CountryListVO;
import com.reyco.dasbx.portal.model.domain.vo.TypeListVO;
import com.reyco.dasbx.portal.model.domain.vo.YearListVO;
import com.reyco.dasbx.portal.service.CategoryService;
import com.reyco.dasbx.portal.service.CountryService;
import com.reyco.dasbx.portal.service.SysVipService;
import com.reyco.dasbx.portal.service.TypeService;
import com.reyco.dasbx.portal.service.VideoPublishListService;
import com.reyco.dasbx.portal.service.YearService;

@Service
public class VideoPublishListServiceImpl implements VideoPublishListService {
	
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
	public Map<String, List<?>> listForAdd() throws Exception {
		Callable<List<CategoryListVO>> categoryTask = new Callable<List<CategoryListVO>>() {
			@Override
			public List<CategoryListVO> call() throws Exception {
				List<CategoryListVO>  categoryListVOs = categoryService.list(); 
				return categoryListVOs;
			}
		};
		Callable<List<CountryListVO>> countryTask= new Callable<List<CountryListVO>>() {
			@Override
			public List<CountryListVO> call() throws Exception {
				List<CountryListVO> countryListVOs = countryService.list();
				return countryListVOs;
			}
		};
		Callable<List<TypeListVO>> typeTask= new Callable<List<TypeListVO>>() {
			@Override
			public List<TypeListVO> call() throws Exception {
				List<TypeListVO> typeListVOs = typeService.list();
				return typeListVOs;
			}
		};
		Callable<List<YearListVO>> yearTask= new Callable<List<YearListVO>>() {
			@Override
			public List<YearListVO> call() throws Exception {
				List<YearListVO> yearListVOs = yearService.list();
				return yearListVOs;
			}
		};
		Callable<List<SysVip>> vipTask= new Callable<List<SysVip>>() {
			@Override
			public List<SysVip> call() throws Exception {
				List<SysVip> sysVips = sysVipService.list();
				return sysVips;
			}
		};
		FutureTask<List<CategoryListVO>> categoryFutureTask = new FutureTask<>(categoryTask);
		new Thread(categoryFutureTask).start();
		FutureTask<List<CountryListVO>> countryFutureTask = new FutureTask<>(countryTask);
		new Thread(countryFutureTask).start();
		FutureTask<List<TypeListVO>> typeFutureTask = new FutureTask<>(typeTask);
		new Thread(typeFutureTask).start();
		FutureTask<List<YearListVO>> yearFutureTask = new FutureTask<>(yearTask);
		new Thread(yearFutureTask).start();
		FutureTask<List<SysVip>> vipFutureTask = new FutureTask<>(vipTask);
		new Thread(vipFutureTask).start();
		List<CategoryListVO> categoryListVOs = categoryFutureTask.get();
		List<CountryListVO> countryListVOs = countryFutureTask.get();
		List<TypeListVO> typeListVOs = typeFutureTask.get();
		List<YearListVO> yearListVOs = yearFutureTask.get();
		List<SysVip> sysVips = vipFutureTask.get();
		Map<String, List<?>> resultMap = new HashMap<String, List<?>>();
		resultMap.put("category", categoryListVOs);
		resultMap.put("country", countryListVOs);
		resultMap.put("type", typeListVOs);
		resultMap.put("year", yearListVOs);
		resultMap.put("vip", sysVips);
		return resultMap;
	}

}
