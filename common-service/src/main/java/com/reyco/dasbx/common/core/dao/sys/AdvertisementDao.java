package com.reyco.dasbx.common.core.dao.sys;

import java.util.List;

import com.reyco.dasbx.common.core.model.domain.sys.Advertisement;

public interface AdvertisementDao {
	
	List<Advertisement> listByType(Byte type);
	
	
}
