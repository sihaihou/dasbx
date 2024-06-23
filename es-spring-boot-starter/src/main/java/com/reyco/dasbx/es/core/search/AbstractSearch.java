package com.reyco.dasbx.es.core.search;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder.Field;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.reyco.dasbx.commons.utils.Page;
import com.reyco.dasbx.es.core.client.ElasticsearchClient;
import com.reyco.dasbx.es.core.client.ElasticsearchDocument;
import com.reyco.dasbx.es.core.model.Aggregation;
import com.reyco.dasbx.es.core.search.type.IndexHighlightType;
import com.reyco.dasbx.es.core.search.type.IndexType;
import com.reyco.dasbx.es.core.utils.ElasticsearchUtils;

public abstract class AbstractSearch<T> implements Search<T> {

	@Autowired
	private ElasticsearchClient<ElasticsearchDocument> elasticsearchClient;

	@Override
	public final ElasticsearchVO<T> search(ElasticsearchDto elasticsearchDto) throws IOException {
		SearchRequest searchRequest = new SearchRequest(getIndexType().getIndexName());
		// 构建FromTo
		buildFromTo(searchRequest, elasticsearchDto);
		// 构建BoolQuery查询
		buildBoolQueryBuilder(searchRequest, elasticsearchDto);
		// 构建高亮属性
		buildHighlightBuilder(searchRequest, elasticsearchDto);
		// 构建排序
		buildSort(searchRequest, elasticsearchDto);
		// 构建聚合
		buildAggregation(searchRequest, elasticsearchDto);
		// 执行前处理
		postProcessBeforeInvoke(searchRequest, elasticsearchDto);
		// 执行处理
		ElasticsearchVO<T> elasticsearchVO = invoke(searchRequest, elasticsearchDto);
		// 执行后处理
		postProcessAfterInvoke(elasticsearchVO, elasticsearchDto);
		return elasticsearchVO;
	}

	/**
	 * 获取索引名称
	 * 
	 * @return
	 */
	public abstract IndexType getIndexType();

	protected void postProcessAfterInvoke(ElasticsearchVO<T> elasticsearchVO, ElasticsearchDto elasticsearchDto) {

	}

	protected void postProcessBeforeInvoke(SearchRequest searchRequest, ElasticsearchDto elasticsearchDto) {

	}

	protected ElasticsearchVO<T> invoke(SearchRequest searchRequest, ElasticsearchDto elasticsearchDto) throws IOException {
		ElasticsearchVO<T> elasticsearchVO = new ElasticsearchVO<T>() {
			SearchResponse searchResponse = elasticsearchClient.restHighLevelClient().search(searchRequest,RequestOptions.DEFAULT);
			@Override
			public Map<String, List<Aggregation>> getAggregations() {
				Map<String, List<Aggregation>> aggregations = invokeAggregations(searchResponse.getAggregations());
				if (!CollectionUtils.isEmpty(aggregations)) {
					proccessAggregations(aggregations);
				}
				return aggregations;
			}
			@Override
			public Page<T> getPage() throws IOException {
				Page<T> page = invokeSearchHits(searchResponse.getHits(), elasticsearchDto);
				return page;
			}
		};
		return elasticsearchVO;
	}

	protected Page<T> invokeSearchHits(SearchHits searchHits, ElasticsearchDto elasticsearchDto) throws IOException {
		TotalHits totalHits = searchHits.getTotalHits();
		int total = (int) totalHits.value;
		List<T> tList = Stream.of(searchHits.getHits()).map(searchHit->{
			T t = null;
			try {
				t = processSearchHit(searchHit);
				proccessHighlightField(t, searchHit.getHighlightFields(), getIndexType().getIndexHighlightType());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return t;
		}).collect(Collectors.toList());
		Page<T> page = new Page<T>();
		page.setList(tList);
		page.setPageNum(elasticsearchDto.getPageNum());
		page.setPageSize(elasticsearchDto.getPageSize());
		page.setTotal(total);
		return page;
	};
	protected abstract T processSearchHit(SearchHit searchHit) throws IOException;
	/**
	 * 执行解析聚合
	 * 
	 * @param aggregations
	 * @return
	 */
	protected Map<String, List<Aggregation>> invokeAggregations(Aggregations aggregations) {
		return ElasticsearchUtils.parseAggregations(aggregations,getIndexType());
	}

	/**
	 * 处理聚合
	 * 
	 * @param aggregations
	 */
	protected void proccessAggregations(Map<String, List<Aggregation>> aggregations) {
		
	}

	protected void proccessHighlightField() {

	}

	protected void buildFromTo(SearchRequest searchRequest, ElasticsearchDto elasticsearchDto) {
		Integer pageSize = elasticsearchDto.getPageSize();
		Integer pageNum = elasticsearchDto.getPageNum();
		if (pageSize > IndexType.DEFAULT_MAX_PAGE_SIZE) {
			pageSize = IndexType.DEFAULT_MAX_PAGE_SIZE;
		}
		if (pageSize < IndexType.DEFAULT_MIN_PAGE_SIZE) {
			pageSize = IndexType.DEFAULT_MIN_PAGE_SIZE;
		}
		if (pageNum > IndexType.DEFAULT_MAX_PAGE_NUM) {
			pageNum = IndexType.DEFAULT_MAX_PAGE_NUM;
		}
		if (pageNum < 1) {
			pageNum = 1;
		}
		if(pageSize*pageNum>IndexType.DEFAULT_TOTAL_SIZE) {
			pageNum = IndexType.DEFAULT_TOTAL_SIZE/pageSize;
		}
		searchRequest.source().from((pageNum - 1) * pageSize).size(pageSize);
	}

	/**
	 * 构建bool查询: bool：bool查询
	 *  must：必须匹配（相当于mysql的'and'，算分） 
	 *  should：或者匹配（相当于mysql的'or'） 
	 * 	must_not：必须不匹配(相当于mysql的'!=') 
	 * 	filter：必须匹配（相当于mysql的'and',不计算分）
	 * @param searchRequest
	 * @param simpleElasticsearchDto
	 */
	protected void buildBoolQueryBuilder(SearchRequest searchRequest, ElasticsearchDto elasticsearchDto) {
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		buildBoolQueryBuilderMust(boolQuery, elasticsearchDto);
		buildBoolQueryBuilderShould(boolQuery, elasticsearchDto);
		buildBoolQueryBuilderMustNot(boolQuery, elasticsearchDto);
		buildBoolQueryBuilderFilter(boolQuery, elasticsearchDto);
		searchRequest.source().query(boolQuery);
	}

	/**
	 * 构建bool查询--must：必须匹配（相当于mysql的'and'，算分）
	 * 
	 * @param boolQuery
	 * @param simpleElasticsearchDto
	 */
	protected void buildBoolQueryBuilderMust(BoolQueryBuilder boolQuery, ElasticsearchDto elasticsearchDto) {
	}

	/**
	 * 构建bool查询--should：或者匹配（相当于mysql的 'or'）
	 * 
	 * @param boolQuery
	 * @param simpleElasticsearchDto
	 */
	protected void buildBoolQueryBuilderShould(BoolQueryBuilder boolQuery, ElasticsearchDto elasticsearchDto) {
	}

	/**
	 * filter：必须匹配（相当于mysql的'and',不计算分）
	 * 
	 * @param boolQuery
	 * @param simpleElasticsearchDto
	 */
	protected void buildBoolQueryBuilderMustNot(BoolQueryBuilder boolQuery, ElasticsearchDto elasticsearchDto) {
	}

	/**
	 * must_not：必须不匹配(相当于mysql的'!=')
	 * 
	 * @param boolQuery
	 * @param simpleElasticsearchDto
	 */
	protected void buildBoolQueryBuilderFilter(BoolQueryBuilder boolQuery, ElasticsearchDto elasticsearchDto) {
	}

	/**
	 * 构建高亮查询
	 * 
	 * @param searchRequest
	 * @param simpleElasticsearchDto
	 */
	private final void buildHighlightBuilder(SearchRequest searchRequest, ElasticsearchDto elasticsearchDto) {
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		buildHighlightBuilder(highlightBuilder, elasticsearchDto);
		searchRequest.source().highlighter(highlightBuilder);
	}

	/**
	 * 构建高亮查询
	 * 
	 * @param highlightBuilder
	 * @param simpleElasticsearchDto
	 */
	protected void buildHighlightBuilder(HighlightBuilder highlightBuilder, ElasticsearchDto elasticsearchDto) {
		String[] highlightFields = getIndexType().getIndexHighlightType().getHighlightFields();
		if (highlightFields != null && highlightFields.length > 0) {
			Stream.of(highlightFields).forEach(highlightField->highlightBuilder.fields().add(new Field(highlightField)));
		}
	}

	/**
	 * 构建高亮查询
	 * 
	 * @param searchRequest
	 * @param simpleElasticsearchDto
	 */
	protected void buildSort(SearchRequest searchRequest, ElasticsearchDto elasticsearchDto) {

	}

	/**
	 * 构建高亮查询
	 * 
	 * @param searchRequest
	 * @param simpleElasticsearchDto
	 */
	protected void buildAggregation(SearchRequest searchRequest, ElasticsearchDto elasticsearchDto) {
		ElasticsearchUtils.buildAggregation(searchRequest, getIndexType().getIndexAggregationType());
	}
	/**
	 * 处理高亮字段
	 * @param elasticsearchDocument
	 * @param highlightFields
	 * @param indexHighlightType
	 * @throws Exception
	 */
	protected void proccessHighlightField(T t,Map<String, HighlightField> highlightFields, IndexHighlightType indexHighlightType) {
		if (!CollectionUtils.isEmpty(highlightFields)) {
			String[] highlightFieldNames = indexHighlightType.getHighlightFields();
			if (highlightFieldNames == null || highlightFieldNames.length == 0) {
				return;
			}
			Stream.of(highlightFieldNames).forEach(highlightFieldName->{
				try {
					Class<?> clazz = t.getClass();
					java.lang.reflect.Field field = clazz.getDeclaredField(highlightFieldName);
					if (field != null && highlightFields.containsKey(highlightFieldName)) {
						HighlightField nameHighlightField = highlightFields.get(highlightFieldName);
						String name = nameHighlightField.getFragments()[0].string();
						field.setAccessible(true);
						field.set(t, name);
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			});
		}
	}
	/**
	 * 
	 * @param genders
	 */
	protected void proccessGenderAggregations(List<Aggregation> genders) {
		if(!CollectionUtils.isEmpty(genders)) {
			genders.stream().forEach(aggs->{
				String value = aggs.getValue();
				aggs.setId(Long.parseLong(value));
				aggs.setValue(value.equals("1")?"男":"女");
			});
		}
	}
}
