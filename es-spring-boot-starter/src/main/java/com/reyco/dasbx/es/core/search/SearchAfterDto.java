package com.reyco.dasbx.es.core.search;

import org.elasticsearch.search.sort.SortOrder;

public interface SearchAfterDto extends SearchDto{
	
	String getSortField();
	
	SortOrder getSortOrder();
	
	String getSortId();
	
}
