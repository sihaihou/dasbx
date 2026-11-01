package com.reyco.dasbx.es.test.scroll;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.reyco.dasbx.es.core.engine.SearchEngine;
import com.reyco.dasbx.es.core.exception.SearchBuildException;
import com.reyco.dasbx.es.core.feature.interceptor.SearchInterceptorPipeline;
import com.reyco.dasbx.es.core.log.DslLogger;
import com.reyco.dasbx.es.core.query.SearchContext;
import com.reyco.dasbx.es.core.query.SearchQuery;
import com.reyco.dasbx.es.core.query.builder.DefaultSearchBuilder;
import com.reyco.dasbx.es.core.query.builder.executor.ElasticsearchSearchExecutor;
import com.reyco.dasbx.es.core.query.builder.executor.SearchExecutor;
import com.reyco.dasbx.es.core.result.SearchResult;
import com.reyco.dasbx.es.core.result.mapper.ResultMapper;
import com.reyco.dasbx.es.core.result.page.Pages;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class ScrollExecutionTest {

	private static final String SCROLL_RESPONSE = "{"
			+ "\"_scroll_id\":\"scroll-2\","
			+ "\"took\":1,"
			+ "\"timed_out\":false,"
			+ "\"_shards\":{\"total\":1,\"successful\":1,\"skipped\":0,\"failed\":0},"
			+ "\"hits\":{"
			+ "\"total\":{\"value\":1,\"relation\":\"eq\"},"
			+ "\"max_score\":null,"
			+ "\"hits\":[{"
			+ "\"_index\":\"product\","
			+ "\"_type\":\"_doc\","
			+ "\"_id\":\"doc-1\","
			+ "\"_score\":null,"
			+ "\"_source\":{\"name\":\"first\"}"
			+ "}]"
			+ "}"
			+ "}";

	private static final String CLEAR_SCROLL_RESPONSE =
			"{\"succeeded\":true,\"num_freed\":1}";

	private final ResultMapper resultMapper = new ResultMapper() {
		@Override
		public <T> SearchResult<T> map(SearchContext context, SearchResponse response, Class<T> clazz) {
			return new SearchResult<>();
		}
	};

	private HttpServer server;
	private RestHighLevelClient client;
	private volatile String requestedMethod;
	private volatile String requestedPath;
	private volatile String requestBody;

	@Before
	public void setUp() throws Exception {
		server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
		server.createContext("/", this::handleRequest);
		server.start();

		RestClientBuilder builder = RestClient.builder(
				new HttpHost("127.0.0.1", server.getAddress().getPort(), "http"));
		client = new RestHighLevelClient(builder);
	}

	@After
	public void tearDown() throws Exception {
		if (client != null) {
			client.close();
		}
		if (server != null) {
			server.stop(0);
		}
	}

	@Test
	public void firstScrollPageUsesSearchAndContinuationUsesScroll() {
		RecorderExecutor executor = new RecorderExecutor();
		SearchEngine engine = new SearchEngine(Collections.emptyList(), executor);

		new DefaultSearchBuilder<Object>(Object.class, engine, resultMapper)
				.page(Pages.scroll(10))
				.search();

		assertEquals(1, executor.executeCount);
		assertEquals(0, executor.scrollCount);

		new DefaultSearchBuilder<Object>(Object.class, engine, resultMapper)
				.page(Pages.scroll("scroll-1", 10))
				.search();

		assertEquals(1, executor.executeCount);
		assertEquals(1, executor.scrollCount);
	}

	@Test
	public void elasticsearchExecutorUsesScrollApi() throws Exception {
		DslLogger logger = mock(DslLogger.class);
		SearchInterceptorPipeline interceptorPipeline = emptyInterceptorPipeline();
		ElasticsearchSearchExecutor executor =
				new ElasticsearchSearchExecutor(client, interceptorPipeline, logger);

		SearchQuery query = new SearchQuery();
		query.setPage(Pages.scroll("scroll-1", 10));
		SearchContext context = new SearchContext(
				Object.class, "product", query, null, null, null);

		SearchResponse actual = executor.scroll(context);

		assertSame(actual, context.getResponse());
		assertNotNull(actual.getScrollId());
		assertEquals("scroll-2", actual.getScrollId());
		assertEquals(1, actual.getHits().getHits().length);
		assertEquals("POST", requestedMethod);
		assertEquals("/_search/scroll", requestedPath);
		assertTrue(requestBody.contains("scroll-1"));
	}

	@Test
	public void clearScrollUsesClearScrollApi() throws Exception {
		DslLogger logger = mock(DslLogger.class);
		SearchInterceptorPipeline interceptorPipeline = emptyInterceptorPipeline();
		ElasticsearchSearchExecutor executor =
				new ElasticsearchSearchExecutor(client, interceptorPipeline, logger);

		assertTrue(executor.clearScroll("scroll-1"));
		assertEquals("DELETE", requestedMethod);
		assertEquals("/_search/scroll", requestedPath);
		assertTrue(requestBody.contains("scroll-1"));
	}

	@Test(expected = SearchBuildException.class)
	public void continuationRequiresScrollId() {
		DslLogger logger = mock(DslLogger.class);
		SearchInterceptorPipeline interceptorPipeline = emptyInterceptorPipeline();
		ElasticsearchSearchExecutor executor =
				new ElasticsearchSearchExecutor(client, interceptorPipeline, logger);

		SearchQuery query = new SearchQuery();
		query.setPage(Pages.scroll(10));
		SearchContext context = new SearchContext(
				Object.class, "product", query, null, null, null);

		executor.scroll(context);
	}

	private SearchInterceptorPipeline emptyInterceptorPipeline() {
		return new SearchInterceptorPipeline(
				Collections.emptyList(),
				Collections.emptyList());
	}

	private void handleRequest(HttpExchange exchange) throws IOException {
		requestedMethod = exchange.getRequestMethod();
		requestedPath = exchange.getRequestURI().getPath();
		requestBody = readBody(exchange.getRequestBody());

		String response = "DELETE".equals(requestedMethod)
				? CLEAR_SCROLL_RESPONSE
				: SCROLL_RESPONSE;
		byte[] body = response.getBytes(StandardCharsets.UTF_8);

		exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
		exchange.sendResponseHeaders(200, body.length);
		try (OutputStream output = exchange.getResponseBody()) {
			output.write(body);
		}
	}

	private String readBody(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = input.read(buffer)) != -1) {
			output.write(buffer, 0, length);
		}
		return output.toString(StandardCharsets.UTF_8.name());
	}

	private static class RecorderExecutor implements SearchExecutor {

		private final SearchResponse response = mock(SearchResponse.class);
		private int executeCount;
		private int scrollCount;

		@Override
		public SearchResponse execute(SearchContext context) {
			executeCount++;
			return response;
		}

		@Override
		public CompletableFuture<SearchResponse> executeAsync(SearchContext context) {
			executeCount++;
			return CompletableFuture.completedFuture(response);
		}

		@Override
		public SearchResponse scroll(SearchContext context) {
			scrollCount++;
			return response;
		}

		@Override
		public CompletableFuture<SearchResponse> scrollAsync(SearchContext context) {
			scrollCount++;
			return CompletableFuture.completedFuture(response);
		}

		@Override
		public boolean clearScroll(String scrollId) {
			return true;
		}
	}
}
