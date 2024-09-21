package com.reyco.dasbx.portal.service.es.video;

import java.io.IOException;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder.Field;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.commons.utils.Convert;
import com.reyco.dasbx.commons.utils.JsonUtils;
import com.reyco.dasbx.es.core.search.AbstractSearch;
import com.reyco.dasbx.es.core.search.SearchDto;
import com.reyco.dasbx.es.core.search.type.IndexHighlightType;
import com.reyco.dasbx.es.core.search.type.IndexType;
import com.reyco.dasbx.es.core.search.type.impl.DefaultIndexHighlightType;
import com.reyco.dasbx.es.core.search.type.impl.DefaultIndexSearchFieldType;
import com.reyco.dasbx.es.core.search.type.impl.DefaultIndexType;
import com.reyco.dasbx.portal.constant.Constants;
import com.reyco.dasbx.portal.model.domain.dto.VideoSearchDto;
import com.reyco.dasbx.portal.model.domain.vo.VideoListVO;
import com.reyco.dasbx.portal.model.es.po.VideoElasticsearchDocument;

/**
 * 
 * @author reyco
 *
 */
@Service
public class VideoSearch extends AbstractSearch<VideoListVO>{
	
	@Override
	public IndexType getIndexType() {
		DefaultIndexType indexType = new DefaultIndexType();
		indexType.setIndexName(Constants.VIDEO_INDEX_NAME);
		DefaultIndexSearchFieldType indexSearchFieldType = new DefaultIndexSearchFieldType();
		indexSearchFieldType.setIndexName(Constants.VIDEO_INDEX_NAME);
		indexSearchFieldType.setSearchField(Constants.VIDEO_SEARCH_FIELD);
		indexSearchFieldType.setMultiFields(Constants.VIDEO_SEARCH_MULTIFIELDS);
		indexType.setIndexSearchFieldType(indexSearchFieldType);
		DefaultIndexHighlightType indexHighlightType = new DefaultIndexHighlightType();
		indexHighlightType.setIndexName(Constants.VIDEO_INDEX_NAME);
		indexHighlightType.setHighlightFields(Constants.VIDEO_HIGHLIGHT_FIELDS);
		indexType.setIndexHighlightType(indexHighlightType);
		return indexType;
	}
	@Override
	protected void buildBoolQueryBuilderMust(BoolQueryBuilder boolQuery, SearchDto elasticsearchDto) {
		VideoSearchDto videoSearchDto = (VideoSearchDto)elasticsearchDto;
		if (StringUtils.isNotBlank(videoSearchDto.getKeyword())) {
			boolQuery.must(QueryBuilders.multiMatchQuery(videoSearchDto.getKeyword(), getIndexType().getIndexSearchFieldType().getMultiFields()));
		}
	}
	@Override
	protected void buildBoolQueryBuilderFilter(BoolQueryBuilder boolQuery, SearchDto elasticsearchDto) {
		VideoSearchDto videoSearchDto = (VideoSearchDto)elasticsearchDto;
		if (videoSearchDto.getCategoryId() != null && videoSearchDto.getCategoryId().intValue()!=-1) {
			boolQuery.filter(QueryBuilders.termQuery("categoryId", videoSearchDto.getCategoryId()));
		}
		if (videoSearchDto.getCountryId() != null && videoSearchDto.getCountryId().intValue()!=-1) {
			boolQuery.filter(QueryBuilders.termQuery("countryId", videoSearchDto.getCountryId()));
		}
		if (videoSearchDto.getTypeId() != null && videoSearchDto.getTypeId().intValue()!=-1) {
			boolQuery.filter(QueryBuilders.termQuery("typeId", videoSearchDto.getTypeId()));
		}
		if (videoSearchDto.getYearId() != null && videoSearchDto.getYearId().intValue()!=-1) {
			boolQuery.filter(QueryBuilders.termQuery("yearId", videoSearchDto.getYearId()));
		}
		if (videoSearchDto.getVipId() != null && videoSearchDto.getVipId().intValue()!=-1) {
			boolQuery.filter(QueryBuilders.termQuery("vipId", videoSearchDto.getVipId()));
		}
	}
	@Override
	protected VideoListVO processSearchHit(SearchHit searchHit) throws IOException {
		VideoElasticsearchDocument videoElasticsearchDocument = JsonUtils.jsonToObj(searchHit.getSourceAsString(), VideoElasticsearchDocument.class);
		VideoListVO videoListVO = Convert.sourceToTarget(videoElasticsearchDocument, VideoListVO.class);
		return videoListVO;
	}
	@Override
	protected void buildHighlightBuilder(HighlightBuilder highlightBuilder, SearchDto searchDto) {
		IndexHighlightType highlightType = getIndexType().getIndexHighlightType();
		String[] highlightFields = null;
		if(highlightType==null || (highlightFields = highlightType.getHighlightFields())==null || highlightFields.length==0) {
			return;
		}
		Stream.of(highlightFields).forEach(highlightField->{
			highlightBuilder.fields().add(new Field(highlightField));
			highlightBuilder.preTags("");
			highlightBuilder.postTags("");
		});
	}
}
