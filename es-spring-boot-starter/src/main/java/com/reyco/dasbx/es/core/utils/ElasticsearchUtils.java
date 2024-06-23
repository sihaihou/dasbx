package com.reyco.dasbx.es.core.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder.Field;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;

import com.reyco.dasbx.es.core.model.Aggregation;
import com.reyco.dasbx.es.core.search.type.IndexAggregationType;
import com.reyco.dasbx.es.core.search.type.IndexSuggestionType;
import com.reyco.dasbx.es.core.search.type.IndexType;

public class ElasticsearchUtils {
	/**
	 * 构建高亮属性
	 * @param highlightFields
	 * @return
	 */
	public static void buildHighlightBuilder(SearchRequest searchRequest,String[] highlightFields) {
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		if(highlightFields!=null && highlightFields.length>0) {
			Stream.of(highlightFields).forEach(highlightField->highlightBuilder.fields().add(new Field(highlightField)));
			searchRequest.source().highlighter(highlightBuilder);
		}
	}
	/**
	 * 构建聚合属性
	 * @param indexType
	 * @return
	 */
	public static void buildAggregation(SearchRequest searchRequest,IndexType indexType) {
		IndexAggregationType indexAggregationType = indexType.getIndexAggregationType();
		String[] aggregationFields = indexAggregationType.getAggregationFields();
		String[] aggregationNames = indexAggregationType.getAggregationNames();
		Integer[] aggregationSizes = indexAggregationType.getAggregationSizes();
		Boolean[]aggregationOrders = indexAggregationType.getAggregationFieldOrders();
		buildAggregation(searchRequest, aggregationFields, aggregationNames, aggregationOrders, aggregationSizes);
	}
	/**
	 * 构建聚合属性
	 * @param indexAggregationType
	 * @return
	 */
	public static void buildAggregation(SearchRequest searchRequest,IndexAggregationType indexAggregationType) {
		String[] aggregationFields = indexAggregationType.getAggregationFields();
		String[] aggregationNames = indexAggregationType.getAggregationNames();
		Integer[] aggregationSizes = indexAggregationType.getAggregationSizes();
		Boolean[]aggregationOrders = indexAggregationType.getAggregationFieldOrders();
		buildAggregation(searchRequest, aggregationFields, aggregationNames, aggregationOrders, aggregationSizes);
	}
	/**
	 * 构建聚合属性
	 * @param aggregationFields
	 * @return
	 */
	public static void buildAggregation(SearchRequest searchRequest,String[] aggregationFields,String[] aggregationNames,Boolean[] aggregationOrders,Integer[] aggregationSizes) {
		if(aggregationFields!=null&&aggregationFields.length>0) {
			int fieldLength = aggregationFields.length;
			if(aggregationNames==null||aggregationNames.length<fieldLength) {
				if(aggregationNames==null) {
					aggregationNames = aggregationFields;
				}else {
					String[] newAggregationFieldNames = new String[fieldLength];
					int nameLength = aggregationNames.length;
					System.arraycopy(aggregationNames,0,newAggregationFieldNames,0,nameLength);
					System.arraycopy(aggregationFields,nameLength,newAggregationFieldNames,nameLength,fieldLength-nameLength);
					aggregationNames=newAggregationFieldNames;
				}
			}
			if(aggregationSizes==null||aggregationSizes.length<fieldLength) {
				if(aggregationSizes==null) {
					aggregationSizes = new Integer[fieldLength];
					for(int i=0;i<fieldLength;i++) {
						aggregationSizes[i] = IndexType.DEFAULT_AGGREGATION_SIZE;
					}
				}else {
					Integer[] newAggregationSizes = new Integer[fieldLength];
					int aggSizelength = aggregationSizes.length;
					System.arraycopy(aggregationSizes,0,newAggregationSizes,0,aggSizelength);
					for(int i=aggSizelength;i<fieldLength;i++) {
						newAggregationSizes[i] = IndexType.DEFAULT_AGGREGATION_SIZE;
					}
					aggregationSizes = newAggregationSizes;
				}
			}
			if(aggregationOrders==null||aggregationOrders.length<fieldLength) {
				if(aggregationOrders==null) {
					aggregationOrders = new Boolean[fieldLength];
					for(int i=0;i<fieldLength;i++) {
						aggregationOrders[i] = IndexType.DEFAULT_AGGREGATION_ORDER;
					}
				}else {
					Boolean[] newAggsFieldOrders = new Boolean[fieldLength];
					int aggsFieldOrderlength = aggregationOrders.length;
					System.arraycopy(aggregationOrders,0,newAggsFieldOrders,0,aggsFieldOrderlength);
					for(int i=aggsFieldOrderlength;i<fieldLength;i++) {
						newAggsFieldOrders[i] = IndexType.DEFAULT_AGGREGATION_ORDER;
					}
					aggregationOrders = newAggsFieldOrders;
				}
			}
			for(int i=0;i<fieldLength;i++) {
				String aggregationField = aggregationFields[i];
				String aggregationName = aggregationNames[i];
				searchRequest.source().aggregation(AggregationBuilders.terms(aggregationName).field(aggregationField)
						.order(BucketOrder.key(aggregationOrders[i])).size(aggregationSizes[i]));
			}
		}
	}
	/**
	 * 构建自动补全属性
	 * @param searchRequest
	 * @param keyword
	 * @param indexSuggestionType
	 */
	public static void buildSuggestion(SearchRequest searchRequest,String keyword,IndexSuggestionType indexSuggestionType) {
		buildSuggestion(searchRequest, keyword, indexSuggestionType.getSuggestionField(),indexSuggestionType.getIndexName(),indexSuggestionType.getSuggestionSize());
	}
	/**
	 * 构建自动补全属性
	 * @param searchRequest
	 * @param keyword
	 */
	public static void buildSuggestion(SearchRequest searchRequest,String keyword) {
		buildSuggestion(searchRequest, keyword, IndexType.DEFAULT_SUGGESTION_FIELD, IndexType.DEFAULT_SUGGESTION_FIELD, IndexType.DEFAULT_SUGGESTION_SIZE);
	}
	/**
	 * 构建自动补全属性
	 * @param searchRequest
	 * @param keyword
	 * @param size
	 */
	public static void buildSuggestion(SearchRequest searchRequest,String keyword,Integer size) {
		buildSuggestion(searchRequest, keyword, IndexType.DEFAULT_SUGGESTION_FIELD, IndexType.DEFAULT_SUGGESTION_FIELD, size);
	}
	/**
	 * 构建自动补全属性
	 * @param searchRequest
	 * @param keyword
	 * @param completionFieldName
	 */
	public static void buildSuggestion(SearchRequest searchRequest,String keyword,String completionFieldName) {
		buildSuggestion(searchRequest, keyword, completionFieldName, completionFieldName, IndexType.DEFAULT_SUGGESTION_SIZE);
	}
	/**
	 * 构建自动补全属性
	 * @param searchRequest
	 * @param keyword
	 * @param completionFieldName
	 * @param size
	 */
	public static void buildSuggestion(SearchRequest searchRequest,String keyword,String completionFieldName,Integer size) {
		buildSuggestion(searchRequest, keyword, completionFieldName, completionFieldName, size);
	}
	/**
	 * 构建自动补全属性
	 * @param searchRequest
	 * @param keyword
	 * @param completionFieldName
	 * @param suggestionName
	 */
	public static void buildSuggestion(SearchRequest searchRequest,String keyword,String completionFieldName,String suggestionName) {
		buildSuggestion(searchRequest, keyword, completionFieldName, suggestionName, IndexType.DEFAULT_SUGGESTION_SIZE);
	}
	/**
	 * 构建自动补全属性
	 * @param searchRequest
	 * @param keyword
	 * @param completionFieldName
	 * @param suggestionName
	 * @param size
	 */
	public static void buildSuggestion(SearchRequest searchRequest,String keyword,String completionFieldName,String suggestionName,Integer size) {
		if(searchRequest==null || StringUtils.isBlank(completionFieldName)) {
			throw new RuntimeException("searchRequest or completionFieldName属性不能未空！");
		}
		if(suggestionName==null) {
			suggestionName = completionFieldName;
		}
		if(size==null || size<IndexType.DEFAULT_SUGGESTION_MIN_SIZE) {
			size = IndexType.DEFAULT_SUGGESTION_MIN_SIZE;
		}
		if(size>IndexType.DEFAULT_SUGGESTION_MAX_SIZE) {
			size = IndexType.DEFAULT_SUGGESTION_MAX_SIZE;
		}
		searchRequest.source().suggest(
				new SuggestBuilder().addSuggestion(suggestionName,
						SuggestBuilders
						.completionSuggestion(completionFieldName)
						.skipDuplicates(true)
						.prefix(keyword)
						.size(size)
				)
			);
	}
	
	
	/**
	 * 解析aggregations聚合结果
	 * @param aggregations
	 * @param aggsNames
	 * @return
	 */
	public static Map<String,List<Aggregation>> parseAggregations(Aggregations aggregations,IndexType indexType){
		return parseAggregations(aggregations, indexType.getIndexAggregationType());
	}
	/**
	 * 解析aggregations聚合结果
	 * @param aggregations
	 * @param aggsNames
	 * @return
	 */
	public static Map<String,List<Aggregation>> parseAggregations(Aggregations aggregations,IndexAggregationType indexAggregationType){
		if((indexAggregationType.getAggregationNames()==null
				|| indexAggregationType.getAggregationNames().length==0)
				&& (indexAggregationType.getAggregationFields()==null
				&& indexAggregationType.getAggregationFields().length==0)
				) {
			return null;
		}
		if(indexAggregationType.getAggregationNames()==null || indexAggregationType.getAggregationNames().length==0) {
			return parseAggregations(aggregations, indexAggregationType.getAggregationFields());
		}
		return parseAggregations(aggregations, indexAggregationType.getAggregationNames());
	}
	/**
	 * 解析aggregations聚合结果
	 * @param aggregations
	 * @param aggsNames
	 * @return
	 */
	public static Map<String,List<Aggregation>> parseAggregations(@NotNull Aggregations aggregations,String... aggregationNames){
		final Map<String,List<Aggregation>> mapAggregationList = new HashMap<>();;
		Stream.of(aggregationNames).forEach(aggregationName->{
			List<Aggregation> aggregationList = parseAggregationListByAggregationName(aggregations, aggregationName);
			mapAggregationList.put(aggregationName, aggregationList);
		});
		return mapAggregationList;
	}
	/**
	 * 获取aggs
	 * @param aggregations
	 * @param aggsName
	 * @return
	 */
	private static List<Aggregation> parseAggregationListByAggregationName(@NotNull Aggregations aggregations,@NotNull String aggregationName){
		return ((Terms) aggregations.get(aggregationName)).getBuckets().stream()
				.map(bucket->new Aggregation(bucket.getKeyAsString()))
				.collect(Collectors.toList());
	}
	/**
	 * 解析字段不去响应信息
	 * @param suggest
	 * @param suggestionName
	 * @return
	 */
	public static List<String> parseSuggest(Suggest suggest,String suggestionName){
		return ((CompletionSuggestion) suggest.getSuggestion(suggestionName)).getOptions().stream().map(option->option.getText().toString()).collect(Collectors.toList());
	}
	
}
