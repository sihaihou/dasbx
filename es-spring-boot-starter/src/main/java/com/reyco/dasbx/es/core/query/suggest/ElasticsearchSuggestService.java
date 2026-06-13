package com.reyco.dasbx.es.core.query.suggest;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;

public class ElasticsearchSuggestService implements SuggestService {
	
	private RestHighLevelClient client;

	public ElasticsearchSuggestService(RestHighLevelClient client) {
		this.client = client;
	}
	
	@Override
	public List<String> suggest(String index, String keyword) throws Exception {
	    return suggest(index, SuggestQuery.DEFAULT_SUGGEST_FIELD, SuggestQuery.DEFAULT_SUGGEST_SIZE, keyword);
	}
	
	@Override
	public List<String> suggest(String index, String keyword, int size) throws Exception {
		return suggest(index, SuggestQuery.DEFAULT_SUGGEST_FIELD, size, keyword);
	}
	
	@Override
	public List<String> suggest(String index, String field, String keyword) throws Exception {
		return suggest(index, field, SuggestQuery.DEFAULT_SUGGEST_SIZE, keyword);
	}
	
	@Override
	public List<String> suggest(String index, String field, int size, String keyword) throws Exception {
		SuggestQuery query = new SuggestQuery();
        query.setKeyword(keyword);
        query.setField(field);
	    query.setSize(size);
	    return suggest(index,query);
	}
	
	@Override
	public List<String> suggest(String index, SuggestQuery query) throws Exception {

		SearchRequest request = new SearchRequest(index);

		SearchSourceBuilder source = new SearchSourceBuilder();

		SuggestBuilder suggestBuilder = new SuggestBuilder();

		CompletionSuggestionBuilder completionSuggestion = SuggestBuilders
				.completionSuggestion(query.getField())
				.prefix(query.getKeyword())
				.skipDuplicates(true)
				.size(query.getSize());

		suggestBuilder.addSuggestion("suggest", completionSuggestion);

		source.suggest(suggestBuilder);

		request.source(source);

		SearchResponse response = client.search(request, RequestOptions.DEFAULT);

		return parseSuggest(response.getSuggest());
	}

	protected List<String> parseSuggest(Suggest suggest) {
		List<String> result = new ArrayList<>();
		if (suggest == null) {
			return result;
		}

		CompletionSuggestion suggestion = suggest.getSuggestion("suggest");
		if (suggestion == null) {
			return result;
		}

		for (CompletionSuggestion.Entry entry : suggestion.getEntries()) {
			for (CompletionSuggestion.Entry.Option option : entry.getOptions()) {
				result.add(option.getText().string());
			}
		}

		return result;
	}

	
}
