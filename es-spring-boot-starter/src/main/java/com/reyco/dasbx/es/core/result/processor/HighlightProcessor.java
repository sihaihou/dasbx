package com.reyco.dasbx.es.core.result.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.result.SearchHitResult;

public class HighlightProcessor implements SearchHitProcessor{

	@Override
	public <T> void process(SearchContext context, SearchHit hit, T entity, SearchHitResult<T> result) {
		//
		Map<String, HighlightField> fields = hit.getHighlightFields();
		if (fields == null || fields.isEmpty()) {
			return;
		}
		//
		Map<String, List<String>> highlights = new HashMap<>();
		for (Map.Entry<String, HighlightField> entry : fields.entrySet()) {
			List<String> fragments = new ArrayList<>();
			Text[] texts = entry.getValue().fragments();
			if (texts != null) {
				for (Text text : texts) {
					fragments.add(text.string());
				}
			}
			highlights.put(entry.getKey(), fragments);
		}
		result.setHighlights(highlights);
	}
	@Override
	public int getOrder() {
		return 1;
	}

}
