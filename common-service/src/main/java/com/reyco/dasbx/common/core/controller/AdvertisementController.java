package com.reyco.dasbx.common.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.dasbx.common.core.model.domain.sys.Advertisement;
import com.reyco.dasbx.common.core.service.AdvertisementService;
import com.reyco.dasbx.commons.domain.R;

@RestController
@RequestMapping("advertisement")
public class AdvertisementController {
	@Autowired
	private AdvertisementService advertisementService;
	
	@GetMapping("top")
	public Object top() {
		List<Advertisement> advertisements = advertisementService.getAdvertisementByTop();
		return R.success(advertisements);
	}
	@GetMapping("left")
	public Object left() {
		List<Advertisement> advertisements = advertisementService.getAdvertisementByLeft();
		return R.success(advertisements);
	}
	@GetMapping("right")
	public Object right() {
		List<Advertisement> advertisements = advertisementService.getAdvertisementByRight();
		return R.success(advertisements);
	}
	@GetMapping("header")
	public Object header() {
		List<Advertisement> advertisements = advertisementService.getAdvertisementByHeader();
		return R.success(advertisements);
	}
}
