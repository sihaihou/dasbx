package com.reyco.dasbx.common.core.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reyco.dasbx.common.core.constant.Constants;
import com.reyco.dasbx.common.core.dao.sys.AreaDao;
import com.reyco.dasbx.common.core.model.dto.personage.PersonageSearchDto;
import com.reyco.dasbx.common.core.model.po.sys.PersonageElasticsearchDocument;
import com.reyco.dasbx.common.core.model.vo.personage.PersonageInfoVO;
import com.reyco.dasbx.commons.utils.net.CusAccessObjectUtil;
import com.reyco.dasbx.commons.utils.net.IPToLongitudeAndLatitudeUtils;
import com.reyco.dasbx.commons.utils.net.RequestUtils;
import com.reyco.dasbx.commons.utils.net.IPToLongitudeAndLatitudeUtils.LongitudeLatitude;
import com.reyco.dasbx.commons.utils.convert.Convert;
import com.reyco.dasbx.commons.utils.convert.JsonUtils;
import com.reyco.dasbx.es.core.model.Aggregation;
import com.reyco.dasbx.es.core.model.GeoPoint;
import com.reyco.dasbx.es.core.search.AbstractSearch;
import com.reyco.dasbx.es.core.search.SearchDto;
import com.reyco.dasbx.es.core.search.type.IndexType;
import com.reyco.dasbx.es.core.search.type.impl.DefaultIndexAggregationType;
import com.reyco.dasbx.es.core.search.type.impl.DefaultIndexHighlightType;
import com.reyco.dasbx.es.core.search.type.impl.DefaultIndexSearchFieldType;
import com.reyco.dasbx.es.core.search.type.impl.DefaultIndexType;

@Service
public class PersonageSearch extends AbstractSearch<PersonageInfoVO>{
	@Autowired
	private AreaDao areaDao;
	@Override
	public IndexType getIndexType() {
		DefaultIndexType indexType = new DefaultIndexType();
		indexType.setIndexName(Constants.PERSONAGE_INDEX_NAME);
		DefaultIndexSearchFieldType indexSearchFieldType = new DefaultIndexSearchFieldType();
		indexSearchFieldType.setIndexName(Constants.PERSONAGE_INDEX_NAME);
		indexSearchFieldType.setSearchField(Constants.PERSONAGE_SEARCH_FIELD);
		indexSearchFieldType.setMultiFields(Constants.PERSONAGE_SEARCH_MULTIFIELDS);
		indexType.setIndexSearchFieldType(indexSearchFieldType);
		DefaultIndexHighlightType indexHighlightType = new DefaultIndexHighlightType();
		indexHighlightType.setIndexName(Constants.PERSONAGE_INDEX_NAME);
		indexHighlightType.setHighlightFields(Constants.PERSONAGE_HIGHLIGHT_FIELDS);
		indexType.setIndexHighlightType(indexHighlightType);
		DefaultIndexAggregationType indexAggregationType = new DefaultIndexAggregationType();
		indexAggregationType.setIndexName(Constants.PERSONAGE_INDEX_NAME);
		indexAggregationType.setAggregationFields(Constants.PERSONAGE_AGGREGATION_FIELDS);
		indexAggregationType.setAggregationNames(Constants.PERSONAGE_AGGREGATION_NAMES);
		indexAggregationType.setAggregationSizes(Constants.PERSONAGE_AGGREGATION_SIZES);
		indexAggregationType.setAggregationFieldOrders(Constants.PERSONAGE_AGGREGATION_FIELD_ORDERS);
		indexType.setIndexAggregationType(indexAggregationType);
		return indexType;
	}
	@Override
	protected void buildBoolQueryBuilderMust(BoolQueryBuilder boolQuery, SearchDto elasticsearchDto) {
		PersonageSearchDto personageSearchDto = (PersonageSearchDto)elasticsearchDto;
		if (StringUtils.isNotBlank(personageSearchDto.getKeyword())) {
			boolQuery.must(QueryBuilders.multiMatchQuery(personageSearchDto.getKeyword(), getIndexType().getIndexSearchFieldType().getMultiFields()));
		}
	}
	@Override
	protected void buildBoolQueryBuilderFilter(BoolQueryBuilder boolQuery, SearchDto elasticsearchDto) {
		PersonageSearchDto personageSearchDto = (PersonageSearchDto)elasticsearchDto;
		if (personageSearchDto.getGender() != null) {
			boolQuery.filter(QueryBuilders.termQuery("gender", personageSearchDto.getGender()));
		}
		if (StringUtils.isNotBlank(personageSearchDto.getMasterpiece())) {
			boolQuery.filter(QueryBuilders.termQuery("masterpiece", personageSearchDto.getMasterpiece()));
		}
	}
	@Override
	protected void buildSort(SearchRequest searchRequest, SearchDto elasticsearchDto) {
		PersonageSearchDto personageSearchDto = (PersonageSearchDto)elasticsearchDto;
		String ip = CusAccessObjectUtil.getIpAddress(RequestUtils.getHttpServletRequest());
		LongitudeLatitude longitudeLatitude = IPToLongitudeAndLatitudeUtils.getLongitudeAndLatitude(ip);
		personageSearchDto.setLatitude(longitudeLatitude.getLat());
		personageSearchDto.setLongitude(longitudeLatitude.getLon());
		if (personageSearchDto.getLatitude() != null || personageSearchDto.getLongitude() != null) {
			searchRequest.source().sort(SortBuilders
					.geoDistanceSort("location", personageSearchDto.getLatitude(), personageSearchDto.getLongitude())
					.order(SortOrder.ASC)
					.unit(DistanceUnit.KILOMETERS));
		}
	}
	@Override
	protected PersonageInfoVO processSearchHit(SearchHit searchHit) throws IOException {
		PersonageElasticsearchDocument personageElasticsearchDocument = JsonUtils.jsonToObj(searchHit.getSourceAsString(), PersonageElasticsearchDocument.class);
		PersonageInfoVO personageInfoVO = Convert.sourceToTarget(personageElasticsearchDocument, PersonageInfoVO.class);
		personageInfoVO.setProvinceDesc(areaDao.getById(personageInfoVO.getProvinceId()).getName());
		personageInfoVO.setCityDesc(areaDao.getById(personageInfoVO.getCityId()).getName());
		personageInfoVO.setDistrictDesc(areaDao.getById(personageInfoVO.getDistrictId()).getName());
		GeoPoint location = personageElasticsearchDocument.getLocation();
		personageInfoVO.setLatitude(location.getLat());
		personageInfoVO.setLongitude(location.getLon());
		Object[] sortValues = searchHit.getSortValues();
		if (sortValues != null && sortValues.length > 0) {
			personageInfoVO.setDistance(new BigDecimal((Double) sortValues[0]).setScale(2, BigDecimal.ROUND_HALF_UP));
		}
		return personageInfoVO;
	}
	@Override
	protected void proccessAggregations(Map<String, List<Aggregation>> aggregations) {
		proccessGenderAggregations(aggregations.get("gender"));
	}
}
