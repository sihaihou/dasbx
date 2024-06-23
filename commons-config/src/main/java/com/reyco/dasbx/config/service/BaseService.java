package com.reyco.dasbx.config.service;

import java.util.List;

import com.reyco.dasbx.model.dto.DeleteDto;
import com.reyco.dasbx.model.dto.InsertDto;
import com.reyco.dasbx.model.dto.ListDto;
import com.reyco.dasbx.model.dto.UpdateDto;
import com.reyco.dasbx.model.vo.InfoVO;
import com.reyco.dasbx.model.vo.ListVO;

public interface BaseService<V extends InfoVO,V1 extends ListVO,T1 extends ListDto,T2 extends UpdateDto,T3 extends InsertDto,T4 extends DeleteDto> {
	
	V get(Long id);
	
	List<V1> list(T1 t1);
	
	void update(T2 t2);
	
	V insert(T3 t3) throws Exception;
	
	void delete(T4 t4);
}
