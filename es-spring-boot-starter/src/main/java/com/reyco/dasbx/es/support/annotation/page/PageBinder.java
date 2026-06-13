package com.reyco.dasbx.es.support.annotation.page;

import com.reyco.dasbx.es.core.result.page.Pages;
import com.reyco.dasbx.es.support.annotation.execptio.SearchAnnotationException;
import com.reyco.dasbx.es.support.annotation.metadata.AnnotationMetadata;
import com.reyco.dasbx.es.support.annotation.metadata.PageMetadata;
import com.reyco.dasbx.es.support.execution.annotation.EsExecutionContext;

public class PageBinder {

	public void bind(Object dto, AnnotationMetadata metadata, EsExecutionContext context) {
		for (PageMetadata meta : metadata.getPages()) {
			try {
				Object value = meta.getField().get(dto);
				if(value==null) {
					throw new SearchAnnotationException("The EsPageParam class that @EsPage must annotate cannot be empty");
				}else if(!(value instanceof EsPageParam)){
					throw new SearchAnnotationException("@EsPage must be annotated on the EsPageParam class");
				}else {
					EsPageParam page = (EsPageParam) value;
					switch (page.getMode()) {
					case OFFSET:
						context.setPage(Pages.offset(page.getPageNum(), page.getPageSize()));
						break;
						
					case SEARCH_AFTER:
						context.setPage(Pages.searchAfter(page.getSearchAfter(), page.getPageSize()));
						break;
	
					case SCROLL:
						context.setPage(Pages.scroll(page.getScrollId(), page.getPageSize()));
						break;
					}
				}
			} catch (Exception e) {
				throw new SearchAnnotationException("@EsPage parser error!" ,e);
			}
		}
	}
}
