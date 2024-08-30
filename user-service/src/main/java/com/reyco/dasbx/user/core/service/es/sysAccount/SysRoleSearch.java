package com.reyco.dasbx.user.core.service.es.sysAccount;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.utils.Convert;
import com.reyco.dasbx.commons.utils.JsonUtils;
import com.reyco.dasbx.es.core.search.AbstractSearch;
import com.reyco.dasbx.es.core.search.SearchDto;
import com.reyco.dasbx.es.core.search.type.IndexType;
import com.reyco.dasbx.es.core.search.type.impl.DefaultIndexAggregationType;
import com.reyco.dasbx.es.core.search.type.impl.DefaultIndexHighlightType;
import com.reyco.dasbx.es.core.search.type.impl.DefaultIndexSearchFieldType;
import com.reyco.dasbx.es.core.search.type.impl.DefaultIndexSuggestionType;
import com.reyco.dasbx.es.core.search.type.impl.DefaultIndexType;
import com.reyco.dasbx.user.core.constant.Constants;
import com.reyco.dasbx.user.core.model.es.po.SysRoleElasticsearchDocument;
import com.reyco.dasbx.user.core.model.vo.sys.SysRoleInfoVO;

@Service
public class SysRoleSearch extends AbstractSearch<SysRoleInfoVO> {
	
	@Override
	public IndexType getIndexType() {
		DefaultIndexType indexType = new DefaultIndexType();
		indexType.setIndexName(Constants.ROLE_INDEX_NAME);
		DefaultIndexSearchFieldType indexSearchFieldType = new DefaultIndexSearchFieldType();
		indexSearchFieldType.setIndexName(Constants.ROLE_INDEX_NAME);
		indexSearchFieldType.setSearchField(Constants.ROLE_SEARCH_FIELD);
		indexSearchFieldType.setMultiFields(Constants.ROLE_SEARCH_MULTIFIELDS);
		indexType.setIndexSearchFieldType(indexSearchFieldType);
		DefaultIndexHighlightType indexHighlightType = new DefaultIndexHighlightType();
		indexHighlightType.setIndexName(Constants.ROLE_INDEX_NAME);
		indexHighlightType.setHighlightFields(Constants.ROLE_HIGHLIGHT_FIELDS);
		indexType.setIndexHighlightType(indexHighlightType);
		DefaultIndexSuggestionType indexSuggestionType = new DefaultIndexSuggestionType();
		indexSuggestionType.setIndexName(Constants.ROLE_INDEX_NAME);
		indexType.setIndexSuggestionType(indexSuggestionType);
		DefaultIndexAggregationType indexAggregationType = new DefaultIndexAggregationType();
		indexAggregationType.setIndexName(Constants.ROLE_INDEX_NAME);
		indexAggregationType.setAggregationFields(Constants.ROLE_AGGREGATION_FIELDS);
		indexAggregationType.setAggregationNames(Constants.ROLE_AGGREGATION_NAMES);
		indexAggregationType.setAggregationSizes(Constants.ROLE_AGGREGATION_SIZES);
		indexAggregationType.setAggregationFieldOrders(Constants.ROLE_AGGREGATION_FIELD_ORDERS);
		indexType.setIndexAggregationType(indexAggregationType);
		return indexType;
	}
	
	@Override
	protected void buildBoolQueryBuilderMust(BoolQueryBuilder boolQuery, SearchDto elasticsearchDto) {
		if (StringUtils.isNotBlank(elasticsearchDto.getKeyword())) {
			boolQuery.must(QueryBuilders.multiMatchQuery(elasticsearchDto.getKeyword(), getIndexType().getIndexSearchFieldType().getMultiFields()));
		}
	}

	@Override
	protected SysRoleInfoVO processSearchHit(SearchHit searchHit) throws IOException {
		SysRoleElasticsearchDocument sysRoleElasticsearchDocument = JsonUtils.jsonToObj(searchHit.getSourceAsString(), SysRoleElasticsearchDocument.class);
		SysRoleInfoVO sysRoleInfoVO = Convert.sourceToTarget(sysRoleElasticsearchDocument, SysRoleInfoVO.class);
		return sysRoleInfoVO;
	}

}
