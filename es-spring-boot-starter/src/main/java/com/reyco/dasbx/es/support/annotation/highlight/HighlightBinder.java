package com.reyco.dasbx.es.support.annotation.highlight;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.reyco.dasbx.es.core.feature.highlight.HighlightDefinition;
import com.reyco.dasbx.es.core.feature.highlight.Highlights;
import com.reyco.dasbx.es.support.annotation.metadata.AnnotationMetadata;
import com.reyco.dasbx.es.support.annotation.metadata.HighlightMetadata;
import com.reyco.dasbx.es.support.execution.annotation.EsExecutionContext;

public class HighlightBinder {

	public void bind(AnnotationMetadata metadata, EsExecutionContext context) {

		List<HighlightDefinition> highlights = context.getHighlights();

		for (HighlightMetadata meta : metadata.getHighlights()) {
			EsHighlightField hl = meta.getAnnotation();
			if (hl == null) {
				continue;
			}
			if(highlights==null) {
				highlights = new ArrayList<>();
				context.setHighlights(highlights);
			}
			String fieldName = StringUtils.hasText(hl.field()) ? hl.field() : meta.getField().getName();
			highlights.add(Highlights.highlight(fieldName, hl.preTags(), hl.postTags()));
		}
	}
}
