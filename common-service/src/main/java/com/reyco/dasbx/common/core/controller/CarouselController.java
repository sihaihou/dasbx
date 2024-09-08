package com.reyco.dasbx.common.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reyco.dasbx.common.core.model.domain.sys.Carousel;
import com.reyco.dasbx.common.core.service.CarouselService;
import com.reyco.dasbx.commons.domain.R;

@RestController
@RequestMapping("carousel")
public class CarouselController {
	@Autowired
	private CarouselService carouselService;
	
	@GetMapping("getCarousels")
	public Object getCarousels() {
		List<Carousel> carousels = carouselService.getCarousels();
		return R.success(carousels);
	}
}
