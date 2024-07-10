package com.reyco.dasbx.es.core.search;

import java.io.IOException;

/**
 * 搜索接口
 * @author reyco
 * @param <T>
 *
 * @param <T>
 */
public interface Search<T> {
	
	SearchVO<T> search(SearchDto searchDto) throws IOException;
	
}
