package com.reyco.dasbx.es.core.query.suggest;

import java.util.List;

/**
 * 提示词
 * @author reyco
 *
 */
public interface SuggestService {
	
	List<String> suggest(String index, String keyword) throws Exception;
	
	List<String> suggest(String index, String keyword, int size) throws Exception;
	
	List<String> suggest(String index,String field, String keyword) throws Exception;
	
	List<String> suggest(String index,String field, int size, String keyword) throws Exception;
	
	List<String> suggest(String index,SuggestQuery query) throws Exception;
	
}
