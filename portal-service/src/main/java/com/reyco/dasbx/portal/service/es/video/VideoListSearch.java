package com.reyco.dasbx.portal.service.es.video;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.reyco.dasbx.commons.utils.Convert;
import com.reyco.dasbx.commons.utils.JsonUtils;
import com.reyco.dasbx.es.core.model.Aggregation;
import com.reyco.dasbx.es.core.search.AbstractSearch;
import com.reyco.dasbx.es.core.search.SearchDto;
import com.reyco.dasbx.es.core.search.type.IndexType;
import com.reyco.dasbx.es.core.search.type.impl.DefaultIndexAggregationType;
import com.reyco.dasbx.es.core.search.type.impl.DefaultIndexHighlightType;
import com.reyco.dasbx.es.core.search.type.impl.DefaultIndexSearchFieldType;
import com.reyco.dasbx.es.core.search.type.impl.DefaultIndexType;
import com.reyco.dasbx.portal.constant.Constants;
import com.reyco.dasbx.portal.model.domain.dto.VideoSearchDto;
import com.reyco.dasbx.portal.model.domain.vo.CategoryListVO;
import com.reyco.dasbx.portal.model.domain.vo.CountryListVO;
import com.reyco.dasbx.portal.model.domain.vo.TypeListVO;
import com.reyco.dasbx.portal.model.domain.vo.VideoListDetailVO;
import com.reyco.dasbx.portal.model.domain.vo.YearListVO;
import com.reyco.dasbx.portal.model.es.po.VideoElasticsearchDocument;
import com.reyco.dasbx.portal.service.CategoryService;
import com.reyco.dasbx.portal.service.CountryService;
import com.reyco.dasbx.portal.service.TypeService;
import com.reyco.dasbx.portal.service.YearService;

/**
 * 
 * @author reyco
 *
 */
@Service
public class VideoListSearch extends AbstractSearch<VideoListDetailVO>{
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CountryService countryService;
	@Autowired
	private TypeService typeService;
	@Autowired
	private YearService yearService;
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
		DefaultIndexAggregationType indexAggregationType = new DefaultIndexAggregationType();
		indexAggregationType.setIndexName(Constants.VIDEO_INDEX_NAME);
		indexAggregationType.setAggregationFields(Constants.VIDEO_AGGREGATION_FIELDS);
		indexAggregationType.setAggregationNames(Constants.VIDEO_AGGREGATION_NAMES);
		indexAggregationType.setAggregationSizes(Constants.VIDEO_AGGREGATION_SIZES);
		indexAggregationType.setAggregationFieldOrders(Constants.VIDEO_AGGREGATION_FIELD_ORDERS);
		indexType.setIndexAggregationType(indexAggregationType);
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
	protected VideoListDetailVO processSearchHit(SearchHit searchHit) throws IOException {
		VideoElasticsearchDocument videoElasticsearchDocument = JsonUtils.jsonToObj(searchHit.getSourceAsString(), VideoElasticsearchDocument.class);
		VideoListDetailVO videoListDetailVO = Convert.sourceToTarget(videoElasticsearchDocument, VideoListDetailVO.class);
		CategoryListVO categoryListVO = categoryService.get(videoListDetailVO.getCategoryId());
		videoListDetailVO.setCategoryName(categoryListVO.getName());
		CountryListVO countryListVO = countryService.get(videoListDetailVO.getCountryId());
		videoListDetailVO.setCountryName(countryListVO.getName());
		TypeListVO typeListVO = typeService.get(videoListDetailVO.getTypeId());
		videoListDetailVO.setTypeName(typeListVO.getName());
		YearListVO yearListVO = yearService.get(videoListDetailVO.getYearId());
		videoListDetailVO.setYearName(yearListVO.getName());
		return videoListDetailVO;
	}
	@Override
	protected void proccessAggregations(Map<String, List<Aggregation>> aggregations) {
		List<Aggregation> categoryAggregations = aggregations.get("category");
		if(!CollectionUtils.isEmpty(categoryAggregations)) {
			categoryAggregations.stream().forEach(aggs->{
				String value = aggs.getValue();
				Long categoryId = Long.parseLong(value);
				CategoryListVO categoryListVO = categoryService.get(categoryId);
				aggs.setId(categoryId);
				aggs.setValue(categoryListVO.getName());
			});
		}
		categoryAggregations.add(0,new Aggregation(-1L,"全部分类"));
		List<Aggregation> countryAggregations = aggregations.get("country");
		if(!CollectionUtils.isEmpty(countryAggregations)) {
			countryAggregations.stream().forEach(aggs->{
				String value = aggs.getValue();
				Long countryId = Long.parseLong(value);
				CountryListVO countryListVO = countryService.get(countryId);
				aggs.setId(countryId);
				aggs.setValue(countryListVO.getName());
			});
		}
		countryAggregations.add(0,new Aggregation(-1L,"全部地区"));
		List<Aggregation> typeAggregations = aggregations.get("type");
		if(!CollectionUtils.isEmpty(typeAggregations)) {
			typeAggregations.stream().forEach(aggs->{
				String value = aggs.getValue();
				Long typeId = Long.parseLong(value);
				TypeListVO typeListVO = typeService.get(typeId);
				aggs.setId(typeId);
				aggs.setValue(typeListVO.getName());
			});
		}
		typeAggregations.add(0,new Aggregation(-1L,"全部类型"));
		List<Aggregation> yearAggregations = aggregations.get("year");
		if(!CollectionUtils.isEmpty(yearAggregations)) {
			yearAggregations.stream().forEach(aggs->{
				String value = aggs.getValue();
				Long yearId = Long.parseLong(value);
				YearListVO yearListVO = yearService.get(yearId);
				aggs.setId(yearId);
				aggs.setValue(yearListVO.getName());
			});
		}
		yearAggregations.add(0,new Aggregation(-1L,"全部年份"));
	}
	@Override
	protected void buildHighlightBuilder(HighlightBuilder highlightBuilder, SearchDto searchDto) {
		String[] highlightFields = getIndexType().getIndexHighlightType().getHighlightFields();
		if (highlightFields != null && highlightFields.length > 0) {
			Stream.of(highlightFields).forEach(highlightField->{
				highlightBuilder.fields().add(new Field(highlightField));
				highlightBuilder.preTags("");
				highlightBuilder.postTags("");
			});
		}
	}
}
