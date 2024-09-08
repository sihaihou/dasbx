package com.reyco.dasbx.common.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.common.core.dao.sys.CarouselDao;
import com.reyco.dasbx.common.core.model.domain.sys.Carousel;
import com.reyco.dasbx.common.core.service.CarouselService;


@Service
public class CarouselServiceImpl implements CarouselService {
	@Autowired
	private CarouselDao carouselDaol;
	
	@Override
	public List<Carousel> getCarousels() {
		List<Carousel> carousels = carouselDaol.list();
		return carousels;
	}
	
}
