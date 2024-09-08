package com.reyco.dasbx.common.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.common.core.dao.sys.AdvertisementDao;
import com.reyco.dasbx.common.core.model.domain.sys.Advertisement;
import com.reyco.dasbx.common.core.service.AdvertisementService;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {
	@Autowired
	private AdvertisementDao advertisementDao;
	@Override
	public List<Advertisement> getAdvertisementByTop() {
		List<Advertisement> advertisement = advertisementDao.listByType((byte)1);
		return advertisement;
	}

	@Override
	public List<Advertisement> getAdvertisementByLeft() {
		List<Advertisement> advertisement = advertisementDao.listByType((byte)2);
		return advertisement;
	}

	@Override
	public List<Advertisement> getAdvertisementByRight() {
		List<Advertisement> advertisement = advertisementDao.listByType((byte)3);
		return advertisement;
	}

	@Override
	public List<Advertisement> getAdvertisementByHeader() {
		List<Advertisement> advertisement = advertisementDao.listByType((byte)4);
		return advertisement;
	}
}
