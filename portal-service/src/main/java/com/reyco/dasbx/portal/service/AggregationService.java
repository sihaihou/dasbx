package com.reyco.dasbx.portal.service;

import java.util.List;
import java.util.Map;

import com.reyco.dasbx.es.core.model.Aggregation;

public interface AggregationService {
	
	List<Aggregations> listCategory(Integer size);
		
	List<List<Aggregations>> listAggregationsByCategoryId(Long categoryId) throws Exception ;
	
	Map<String,List<Aggregations>> listAggregationMapByCategoryId(Long categoryId) throws Exception ;
	
	public class Aggregations extends Aggregation{
		/**
		 * 
		 */
		private static final long serialVersionUID = -2330240412343898159L;
		private Byte type;
		public Byte getType() {
			return type;
		}
		public void setType(Byte type) {
			this.type = type;
		}
	}
}
