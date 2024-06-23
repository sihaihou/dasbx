package com.reyco.dasbx.user.core.model.es.search.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.reyco.dasbx.commons.utils.Convert;
import com.reyco.dasbx.commons.utils.JsonUtils;
import com.reyco.dasbx.es.core.model.Aggregation;
import com.reyco.dasbx.es.core.search.AbstractSearch;
import com.reyco.dasbx.es.core.search.ElasticsearchDto;
import com.reyco.dasbx.es.core.search.type.IndexType;
import com.reyco.dasbx.es.core.search.type.impl.DefaultIndexAggregationType;
import com.reyco.dasbx.es.core.search.type.impl.DefaultIndexHighlightType;
import com.reyco.dasbx.es.core.search.type.impl.DefaultIndexSearchFieldType;
import com.reyco.dasbx.es.core.search.type.impl.DefaultIndexSuggestionType;
import com.reyco.dasbx.es.core.search.type.impl.DefaultIndexType;
import com.reyco.dasbx.model.constants.AccountType;
import com.reyco.dasbx.user.core.constant.Constants;
import com.reyco.dasbx.user.core.model.dto.sys.SysAccountSearchDto;
import com.reyco.dasbx.user.core.model.es.po.SysAccountElasticsearchDocument;
import com.reyco.dasbx.user.core.model.vo.SysAccountInfoVO;

@Service
public class SysAccountSearch extends AbstractSearch<SysAccountInfoVO>{
	@Override
	public IndexType getIndexType() {
		DefaultIndexType indexType = new DefaultIndexType();
		indexType.setIndexName(Constants.ACCOUNT_INDEX_NAME);
		DefaultIndexSearchFieldType indexSearchFieldType = new DefaultIndexSearchFieldType();
		indexSearchFieldType.setIndexName(Constants.ACCOUNT_INDEX_NAME);
		indexSearchFieldType.setSearchField(Constants.ACCOUNT_SEARCH_FIELD);
		indexSearchFieldType.setMultiFields(Constants.ACCOUNT_SEARCH_MULTIFIELDS);
		indexType.setIndexSearchFieldType(indexSearchFieldType);
		DefaultIndexHighlightType indexHighlightType = new DefaultIndexHighlightType();
		indexHighlightType.setIndexName(Constants.ACCOUNT_INDEX_NAME);
		indexHighlightType.setHighlightFields(Constants.ACCOUNT_HIGHLIGHT_FIELDS);
		indexType.setIndexHighlightType(indexHighlightType);
		DefaultIndexSuggestionType indexSuggestionType = new DefaultIndexSuggestionType();
		indexSuggestionType.setIndexName(Constants.ACCOUNT_INDEX_NAME);
		indexType.setIndexSuggestionType(indexSuggestionType);
		DefaultIndexAggregationType indexAggregationType = new DefaultIndexAggregationType();
		indexAggregationType.setIndexName(Constants.ACCOUNT_INDEX_NAME);
		indexAggregationType.setAggregationFields(Constants.ACCOUNT_AGGREGATION_FIELDS);
		indexAggregationType.setAggregationNames(Constants.ACCOUNT_AGGREGATION_NAMES);
		indexAggregationType.setAggregationSizes(Constants.ACCOUNT_AGGREGATION_SIZES);
		indexAggregationType.setAggregationFieldOrders(Constants.ACCOUNT_AGGREGATION_FIELD_ORDERS);
		indexType.setIndexAggregationType(indexAggregationType);
		return indexType;
	}
	@Override
	protected void buildBoolQueryBuilderMust(BoolQueryBuilder boolQuery, ElasticsearchDto elasticsearchDto) {
		SysAccountSearchDto sysAccountSearchDto = (SysAccountSearchDto)elasticsearchDto;
		if (StringUtils.isNotBlank(sysAccountSearchDto.getKeyword())) {
			boolQuery.must(QueryBuilders.multiMatchQuery(sysAccountSearchDto.getKeyword(), getIndexType().getIndexSearchFieldType().getMultiFields()));
		}
	}
	@Override
	protected void buildBoolQueryBuilderFilter(BoolQueryBuilder boolQuery, ElasticsearchDto elasticsearchDto) {
		SysAccountSearchDto sysAccountSearchDto = (SysAccountSearchDto)elasticsearchDto;
		if (sysAccountSearchDto.getGender() != null) {
			boolQuery.filter(QueryBuilders.termQuery("gender", sysAccountSearchDto.getGender()));
		}
		if (sysAccountSearchDto.getType() != null) {
			boolQuery.filter(QueryBuilders.termQuery("type", sysAccountSearchDto.getType()));
		}
		if (sysAccountSearchDto.getState()!= null) {
			boolQuery.filter(QueryBuilders.termQuery("state", sysAccountSearchDto.getState()));
		}
	}
	@Override
	protected SysAccountInfoVO processSearchHit(SearchHit searchHit) throws IOException {
		SysAccountElasticsearchDocument sysAccountElasticsearchDocument = JsonUtils.jsonToObj(searchHit.getSourceAsString(),SysAccountElasticsearchDocument.class);
		SysAccountInfoVO sysAccountInfoVO = Convert.sourceToTarget(sysAccountElasticsearchDocument, SysAccountInfoVO.class);
		return sysAccountInfoVO;
	}
	@Override
	protected void proccessAggregations(Map<String, List<Aggregation>> aggregations) {
		List<Aggregation> genders = aggregations.get("gender");
		proccessGenderAggregations(genders);
		
		List<Aggregation> types = aggregations.get("type");
		if(!CollectionUtils.isEmpty(types)) {
			types.stream().forEach(aggs->{
				String value = aggs.getValue();
				aggs.setId(Long.parseLong(value));
				aggs.setValue(AccountType.getAccountType(new Byte(value).byteValue()).getRemark());
			});
		}
		List<Aggregation> states = aggregations.get("state");
		if(!CollectionUtils.isEmpty(states)) {
			states.forEach(aggs->{
				String value = aggs.getValue();
				aggs.setId(Long.parseLong(value));
				aggs.setValue(value.equals("0")?"正常":"禁用");
			});
		}
	}
}
