package com.reyco.dasbx.es.support.annotation.page;

import org.elasticsearch.common.unit.TimeValue;

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
					    String keepAlive = page.getScroll();
					    if (keepAlive == null || keepAlive.trim().isEmpty()) {
					        throw new SearchAnnotationException(
					                "@EsPage scroll keepAlive must not be empty");
					    }
					    TimeValue keepAliveValue = TimeValue.parseTimeValue(keepAlive, "scroll keepAlive");
					    if (keepAliveValue.millis() <= 0) {
					        throw new SearchAnnotationException(
					                "@EsPage scroll keepAlive must be greater than zero");
					    }
						context.setPage(Pages.scroll(page.getScrollId(), page.getPageSize(),page.getScroll()));
						break;
					}
				}
			} catch (Exception e) {
				throw new SearchAnnotationException("@EsPage parser error!" ,e);
			}
		}
	}
}
