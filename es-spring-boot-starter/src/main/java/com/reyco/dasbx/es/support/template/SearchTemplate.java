package com.reyco.dasbx.es.support.template;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.reyco.dasbx.es.core.explain.SearchExplain;
import com.reyco.dasbx.es.core.profile.SearchProfile;
import com.reyco.dasbx.es.core.query.builder.SearchBuilder;
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

	private final SearchBuilderFactory factory;

	private final ResultProcessor resultProcessor;

	private final SuggestService suggestService;

	private final EsExecutionFactory executionFactory;

	public SearchTemplate(SearchBuilderFactory factory, ResultProcessor resultProcessor, SuggestService suggestService,
			EsExecutionFactory executionFactory) {

		Assert.notNull(factory, "SearchBuilderFactory must not be null");
		Assert.notNull(resultProcessor, "ResultProcessor must not be null");
		Assert.notNull(suggestService, "SuggestService must not be null");
		Assert.notNull(executionFactory, "EsExecutionFactory must not be null");

		this.factory = factory;
		this.resultProcessor = resultProcessor;
		this.suggestService = suggestService;
		this.executionFactory = executionFactory;
	}

	/**
	 * 核心统一泛型检索方法
	 *
	 * @param queryDto
	 *            查询入参对象
	 * @param resultClass
	 *            返回的实体类型
	 * @param <Q>
	 *            查询 DTO 泛型
	 * @param <R>
	 *            返回 VO 泛型
	 * @return 统一封装的 Result 结果集
	 */
	public <Q, R> Result<R> search(Q queryDto, Class<R> resultClass) {
		Assert.notNull(queryDto, "queryDto must not be null");
		Assert.notNull(resultClass, "resultClass must not be null");

		// 1. 解析注解，生成查询参数
		EsExecutionContext executionContext = executionFactory.create(queryDto);
		Assert.hasText(executionContext.getIndex(), "ES index must not be empty");

		// 2. 构建基础查询
		SearchBuilder<R> builder = factory.builder(resultClass)
				.index(executionContext.getIndex())
				.page(executionContext.getPage())
				.query(executionContext.getBoolCondition())
				.aggregation(executionContext.getAggregations())
				.highlight(executionContext.getHighlights())
				.sorts(executionContext.getSorts());

		// 3. 应用路由注解
		if (executionContext.getRouting() != null && !executionContext.getRouting().trim().isEmpty()) {
			builder.routing(executionContext.getRouting());
		}

		// 4. 应用返回字段控制
		List<String> includeFields = executionContext.getIncludeFields();
		if (includeFields != null && !includeFields.isEmpty()) {
			builder.includeFields(includeFields.toArray(new String[0]));
		}

		List<String> excludeFields = executionContext.getExcludeFields();
		if (excludeFields != null && !excludeFields.isEmpty()) {
			builder.excludeFields(excludeFields.toArray(new String[0]));
		}

		// 5. explain/profile 只在调试时开启，避免每次请求都序列化 DSL
		if (log.isDebugEnabled()) {
			builder.profile().explain();
		}

		SearchResult<R> searchResult = builder.search();

		// 6. 输出调试信息
		if (log.isDebugEnabled()) {
			SearchExplain explain = searchResult.getExplain();
			if (explain != null) {
				log.debug("{}", explain);
			}

			SearchProfile profile = searchResult.getProfile();
			if (profile != null) {
				log.debug("{}", profile);
			}
		}

		// 7. 统一处理高亮回填、分页和聚合结果
		return resultProcessor.process(searchResult, executionContext.getProviderMap());
	}

	public List<String> suggest(String index, String keyword) throws Exception {
		return suggestService.suggest(index, keyword);
	}

	public List<String> suggest(String index, String keyword, int size) throws Exception {
		return suggestService.suggest(index, keyword, size);
	}

	public List<String> suggest(String index, String field, String keyword) throws Exception {
		return suggestService.suggest(index, field, keyword);
	}

	public List<String> suggest(String index, String field, int size, String keyword) throws Exception {
		return suggestService.suggest(index, field, size, keyword);
	}

	public List<String> suggest(String index, SuggestQuery query) throws Exception {
		return suggestService.suggest(index, query);
	}
}
