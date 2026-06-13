package com.reyco.dasbx.es.support.template;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reyco.dasbx.es.core.explain.SearchExplain;
import com.reyco.dasbx.es.core.profile.SearchProfile;
import com.reyco.dasbx.es.core.query.builder.factory.SearchBuilderFactory;
import com.reyco.dasbx.es.core.query.suggest.SuggestQuery;
import com.reyco.dasbx.es.core.query.suggest.SuggestService;
import com.reyco.dasbx.es.core.result.SearchResult;
import com.reyco.dasbx.es.support.execution.annotation.EsExecutionContext;
import com.reyco.dasbx.es.support.execution.annotation.EsExecutionFactory;
import com.reyco.dasbx.es.support.result.Result;
import com.reyco.dasbx.es.support.result.ResultProcessor;

public class SearchTemplate {
	
	protected Logger log = LoggerFactory.getLogger(SearchTemplate.class);
	
	private SearchBuilderFactory factory;
	
	private ResultProcessor resultProcessor;
	
	private SuggestService suggestService;
	
    public SearchTemplate(SearchBuilderFactory factory,ResultProcessor resultProcessor,SuggestService suggestService) {
		super();
		this.factory = factory;
		this.resultProcessor = resultProcessor;
		this.suggestService = suggestService;
	}
	/**
     * 核心统一泛型检索方法
     * * @param queryDto 查询入参对象 (DTO)
     * @param resultClass 返回的实体类型 (VO)
     * @param <Q> 查询 DTO 泛型限制
     * @param <R> 返回 VO 泛型限制
     * @return 统一封装的 Result 结果集
     */
    public <Q,R> Result<R> search(Q queryDto, Class<R> resultClass) {
        // 1. 解析注解契约，吐出执行大盘（Index、BoolCondition、Aggs、Highlights、sorts、Providers）
        EsExecutionContext esExecutionContext = EsExecutionFactory.create(queryDto);
        
        // 2. 链式调用底层 Builder 组装向 ES 发起网络请求
        SearchResult<R> searchResult = factory.builder(resultClass)
                .index(esExecutionContext.getIndex())
                .page(esExecutionContext.getPage())
                .query(esExecutionContext.getBoolCondition())
                .aggregation(esExecutionContext.getAggregations())
                .highlight(esExecutionContext.getHighlights())
                .sorts(esExecutionContext.getSorts())
                .profile()
                .explain()
                .search();
        
        // 3.explain、profile
        if(log.isDebugEnabled()) {
        	SearchExplain explain = searchResult.getExplain();
        	if(explain!=null) {
        		log.debug(explain.toString());
        	}
        	
        	SearchProfile profile = searchResult.getProfile();
        	if(profile!=null) {
        		log.debug(profile.toString());
        	}
        }
        // 4. 统一高亮回填翻译与结果转换，直接返回最终闭环成品
        return resultProcessor.process(searchResult, esExecutionContext.getProviderMap());
    }
    
    public List<String> suggest(String index, String keyword) throws Exception{
    	return suggestService.suggest(index, keyword);
    }
	
    public List<String> suggest(String index, String keyword, int size) throws Exception{
    	return suggestService.suggest(index, keyword, size);
    }
	
    public List<String> suggest(String index,String field, String keyword) throws Exception{
    	return suggestService.suggest(index, field, keyword);
    }
	
    public List<String> suggest(String index,String field, int size, String keyword) throws Exception{
    	return suggestService.suggest(index, field, size, keyword);
    }
	
    public List<String> suggest(String index,SuggestQuery query) throws Exception{
    	return suggestService.suggest(index, query);
    }
}
