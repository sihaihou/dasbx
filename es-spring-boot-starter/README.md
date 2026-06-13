执行顺序:
SearchBuilderFactory 
	↓ 
SearchBuilder 
	↓ 
SearchEngine 
 	↓ 
Pipeline
	↓ 
SearchExecutor 
	↓ 
Elasticsearch
	↓ 
ResultMapper 
	↓ 
SearchResult


sortPipeline
	↓
Strategy


queryPipeline 
	↓ 
adaptor



```java
@Service
public class App {

	@Autowired
	private SearchBuilderFactory factory;

	public SearchResult<Product> search(String keyword) throws Exception {
		return factory.builder(Product.class)
				.index("product")
				.page(Pages.offset(1, 10))
				.sort(Sorts.field("price"))
				.highlight(Highlights.highlight("title"))
				.aggregation(
						Aggregations
						.termsAgg("brandAgg", "brandId")
						.addChild(
								Aggregations
								.avgAgg("priceAvg",
				                "price")
						)
				)
				.query(
						Querys.bool()
						.must(Querys.match("title", keyword))
						.filter(Querys.term("status", 1))
						.must(Querys.range("price").gte(100).lte(5000))
				)
				.search();
	}
	public SearchResult<Product> searchAsync(String keyword) throws Exception {
		 CompletableFuture<SearchResult<Product>> future = factory.builder(Product.class)
				.index("product")
				.page(Pages.offset(1, 10))
				.sort(Sorts.field("price"))
				.highlight(Highlights.highlight("title"))
				.aggregation(
						Aggregations
						.termsAgg("brandAgg", "brandId")
						.addChild(
								Aggregations
								.avgAgg("priceAvg","price")
						)
				)
				.query(
						Querys.bool()
						.must(Querys.match("title", keyword))
						.filter(Querys.term("status", 1))
						.must(Querys.range("price").gte(100).lte(5000))
				)
				.searchAsync();
		 return future.get();
	}
	public Mono<SearchResult<Product>> searchReactive(String keyword) throws Exception {
		return factory.builder(Product.class)
				.index("product")
				.page(Pages.offset(1, 10))
				.sort(Sorts.field("price"))
				.highlight(Highlights.highlight("title"))
				.aggregation(
						Aggregations
						.termsAgg("brandAgg", "brandId")
						.addChild(
								Aggregations
								.avgAgg("priceAvg","price")
						)
				)
				.query(
						Querys.bool()
						.must(Querys.match("title", keyword))
						.filter(Querys.term("status", 1))
						.must(Querys.range("price").gte(100).lte(5000))
				)
				.reactive()
				.mono();
	}
	public Flux<SearchHitResult<Product>> searchFlux(String keyword) throws Exception {
		return factory.builder(Product.class)
				.index("product")
				.page(Pages.offset(1, 10))
				.sort(Sorts.field("price"))
				.reactive()
				.flux();
	}
	public void searchAfterFlux(String keyword) throws Exception {
		 factory.builder(Product.class)
				.index("product")
				.page(Pages.searchAfter(1000))
				.sort(Sorts.field("price"))
				.sort(Sorts.field("_id",SortOrder.ASC))
				.reactive()
				.searchAfterFlux()
				.subscribe(
					System.out::println
				);
	}
}
```