package com.reyco.dasbx.gateway.core.test;

import java.time.Duration;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.retry.Retry;

public class Test {
	public static void main(String[] args) throws InterruptedException {
		
		/*Flux.just("a","b","c")
			.subscribe(System.out::println);
		Mono.just(Arrays.asList("a","b","c"))
			.flatMapMany(Flux::fromIterable)
			.subscribe(System.out::println);*/
		/**
		 * 合并类操作符
		 * 
		 */
		/**
		 * zipWith:
		 * zipWith 操作符可以将两个流合并成一个流，合并的方式是将两个流中的元素按照顺序一一对应，
		 * 然后将两个元素组合成一个元素。 如果两个流的长度不一致，那么最终合并成的流的长度就是两个流中长度较短的那个流的长度。
		 */
		/*Flux.zip(Flux.just("a","b","c"),Flux.just("d","e","f"))
        	.subscribe(System.out::println);
		Flux.just("a","b","c")
			.zipWith(Flux.just("d","e","f"))
        	.subscribe(System.out::println);*/
		/**
		 * mergeWith:
		 * mergeWith 操作符可以将两个流合并成一个流，合并的方式是将两个流中的元素交替地
		 * 放入到合并后的流中。同时运行，根据时间先后运行。
		 */
		/*Flux.merge(Flux.just("a","b","c"),Flux.just("d","e","f"))
        	.subscribe(System.out::println);*/
		/*Flux.just("a","b","c")
			.mergeWith(Flux.just("d","e","f"))
			.subscribe(System.out::println);*/
		/**
		 * concatWith:
		 * concatWith 操作符可以将两个流合并成一个流，合并的方式是将两个流中的元素按照顺序
		 * 放入到合并后的流中。按照顺序分别运行，flux1运行完成以后再运行flux2
		 */
		/*Flux.concat(Flux.just("a","b","c"),Flux.just("d","e","f"))
        	.subscribe(System.out::println);*/
		/*Flux.just("a","b","c")
			.concatWith(Flux.just("d","e","f"))
			.subscribe(System.out::println);*/
		
		/**
		 * 统计和判断类
		 */
		/**
		 * count 操作符可以统计流中元素的个数。
		 * all 操作符可以判断流中的所有元素是否都满足某个条件。
		 * any 操作符可以判断流中是否存在满足某个条件的元素。
		 * hasElements 操作符可以判断流中是否存在元素。
		 */
		/*Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        	.count()
        	.subscribe(System.out::println);*/

		/*Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .all(i -> i > 0)
            .subscribe(System.out::println);*/

		/*Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .any(i -> i > 5)
            .subscribe(System.out::println);*/

		/*Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .hasElements()
            .subscribe(System.out::println);*/
        
        /**
         * 取出和跳过元素
         */
        /**
         * take, takeLast, takeUntil, takeWhile 这四个操作符都是用来从流中取出元素的。
         * 和 java 8 中的 Stream 中的 limit,skip,findFirst,findAny 方法类似。 
         * take 操作符可以从流中取出指定个数的元素。 
         * takeLast 操作符可以从流中取出最后指定个数的元素。 
         * takeUntil 操作符可以从流中取出元素直到满足某个条件。 
         * takeWhile 操作符可以从流中取出元素直到不满足某个条件。
         */
       /*Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        	.take(5)
        	.subscribe(System.out::println);*/

       /* Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .takeLast(5)
            .subscribe(System.out::println);*/

       /* Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
	        .takeUntil(i -> i > 5)
	        .subscribe(System.out::println);*/

        /*Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .takeWhile(i -> i < 5)
            .subscribe(System.out::println);*/
        /**
         * 
         * skip, skipLast, skipUntil, skipWhile 这四个操作符都是用来跳过流中的元素的。
         * 和 java 8 中的 Stream 中的 limit,skip,findFirst,findAny 方法类似。 
         * skip 操作符可以跳过流中指定个数的元素。 
         * skipLast 操作符可以跳过流中最后指定个数的元素。 
         * skipUntil 操作符可以跳过流中元素直到满足某个条件。 
         * skipWhile 操作符可以跳过流中元素直到不满足某个条件。
         */
       /* Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        .skip(5)
        .subscribe(System.out::println);*/

        /*Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .skipLast(5)
            .subscribe(System.out::println);*/

       /* Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .skipUntil(i -> i > 5)
            .subscribe(System.out::println);*/

       /* Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .skipWhile(i -> i < 5)
            .subscribe(System.out::println);*/
        /**
         * 去重操作
         */
        /**
         * distinct, distinctUntilChanged 这两个操作符都是用来去重的。
         * 和 java 8 中的 Stream 中的 distinct 方法类似。 
         * distinct 操作符可以去除流中重复的元素。 
         * distinctUntilChanged 操作符可以去除流中连续重复的元素。
         */
       /*Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5)
        	.distinct()
        	.subscribe(System.out::println);*/

       /*Flux.just(1, 2, 2, 3, 3, 4, 5, 5, 6, 7, 8, 9, 10)
            .distinctUntilChanged()
            .subscribe(System.out::println);*/
        /**
         * 转化为collection或Map的操作
         */
        /**
         * collectList:
         * collectList 操作符可以将流中的元素收集到一个 List 中。
         */
        /*Flux.just("a","b","c")
        .collectList()
        .subscribe(System.out::println);*/
        /**
         * collectMap:
         * collectMap 操作符可以将流中的元素收集到一个 Map 中。
         */ 
        /*Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        	.collectMap(i -> i % 2 == 0 ? "even" : "odd", i -> i)
        	.subscribe(System.out::println);*/
		
		 /**
         * collectSortedList:
         * collectSortedList 操作符可以将流中的元素收集到一个有序的 List 中。
         */ 
		/*Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        	.collectSortedList()
        	.subscribe(System.out::println);*/
		 /**
         * collectMultimap:
         * collectMultimap 操作符可以将流中的元素收集到一个 Multimap 中。
         */ 
		/*Flux.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        	.collectMultimap(i -> i % 2 == 0 ? "even" : "odd")
        	.subscribe(System.out::println);*/
		/**
		 * 对空值的处理
		 */
		/**
		 * defaultIfEmpty:
		 * defaultIfEmpty 操作符可以在流中没有元素时发出一个默认值。
		 */
		/*Flux.empty()
        	.defaultIfEmpty("default")
        	.subscribe(System.out::println);*/
		/**
		 * switchIfEmpty:
		 * switchIfEmpty 操作符可以在流中没有元素时切换到另一个流。
		 */
		/*Flux.empty()
        	.switchIfEmpty(Flux.just(1, 2, 3))
        	.subscribe(System.out::println);*/
		
		/**
		 * 对错误的处理
		 */
		/**
		 * onErrorReturn:
		 * onErrorReturn 操作符可以在流中发生错误时发出一个默认值。
		 */
		/*Flux.just(1, 2, 3)
        	.concatWith(Mono.error(new RuntimeException()))
        	.onErrorReturn(0)
        	.subscribe(System.out::println);*/
		/**
		 * onErrorResume:
		 * onErrorResume 操作符可以在流中发生错误时切换到另一个流。
		 */
		/*Flux.just(1, 2, 3)
        	.concatWith(Mono.error(new RuntimeException()))
        	.onErrorResume(e -> Flux.just(4, 5, 6))
        	.subscribe(System.out::println);*/
		/**
		 * onErrorContinue:
		 * onErrorContinue 操作符可以在流中发生错误时继续发出流中的元素。
		 */
		/*Flux.just(1, 2, 3)
        	.concatWith(Mono.error(new RuntimeException()))
        	.onErrorContinue((e, o) -> System.out.println("error: " + e.getMessage() + ", object: " + o))
        	.subscribe(System.out::println);*/
		/**
		 * retry:
		 * retry  操作符可以在流中发生错误时重试。
		 */
		/*Flux.just(1, 2, 3)
        	.concatWith(Mono.error(new RuntimeException()))
        	.retry(1)
        	.subscribe(System.out::println);*/
		/**
		 * retryWhen:
		 * retryWhen 操作符可以在流中发生错误时根据指定的条件重试。
		 */
		
		/**
		 * timeout:
		 * timeout 操作符可以在流中发生超时时发出一个默认值。
		 */
		/*Flux.just(1, 2, 3)
        	.delayElements(Duration.ofSeconds(1))
        	.timeout(Duration.ofMillis(500), Mono.just(0))
        	.subscribe(System.out::println);
        	Thread.sleep(100000);*/
		/**
		 * timeoutTo:
		 * timeoutTo 操作符可以在流中发生超时时切换到另一个流。
		 */
		/*Flux.just(1, 2, 3)
        	.delayElements(Duration.ofSeconds(1))
        	.timeout(Duration.ofMillis(500), Flux.just(4, 5, 6))
        	.subscribe(System.out::println);
        	Thread.sleep(100000);*/
		/**
		 * 延迟操作
		 */
		/**
		 * delayElements:
		 * delayElements 操作符可以在流中的每个元素发出时延迟一段时间。
		 */
		/*Flux.just(1, 2, 3)
        	.delayElements(Duration.ofSeconds(1))
        	.subscribe(System.out::println);
        	Thread.sleep(100000);*/
		/**
		 * delaySequence:
		 * delaySequence 操作符可以在流中的所有元素发出时延迟一段时间。
		 */
		/*Flux.just(1, 2, 3)
        	.delaySequence(Duration.ofSeconds(1))
        	.subscribe(System.out::println);
        	Thread.sleep(100000);*/
		/**
		 * elapsed:
		 * elapsed 操作符用来计算流中元素的发出时间间隔。
		 */
		/*Flux.interval(Duration.ofMillis(250))
	        .map(input -> {
	        	if (input < 3) return "tick " + input;
	        	throw new RuntimeException("boom");
	        })
	        .elapsed()
	        .retry(1)
	        .subscribe(System.out::println, System.err::println);
			Thread.sleep(2100);*/
		/**
		 * delaySubscription：
		 * delaySubscription 操作符可以在订阅流时延迟一段时间。
		 */
		/*Flux.just(1, 2, 3)
        	.delaySubscription(Duration.ofSeconds(1))
        	.subscribe(System.out::println);
			Thread.sleep(2100);*/
		/**
		 * delaySubscriptionElements:
		 * delaySubscriptionElements 操作符可以在流中的每个元素发出时延迟一段时间。
		 */
		/**
		 * 重复操作
		 */
		/**
		 * repeat:
		 * repeat 操作符可以重复流中的元素。
		 */
		/*Flux.just(1, 2, 3)
        	.repeat(2)
        	.subscribe(System.out::println);*/
		/**
		 * repeatWhen
		 * repeatWhen 操作符可以根据指定的条件重复流中的元素。
		 */
		/**
		 * 对整个数据源的状态进行的操作
		 */
		/**
		 * doOnNext:
		 * doOnNext 操作符可以在流中的每个元素发出时执行指定的操作。
		 */
		/*Flux.just(1, 2, 3)
			.doOnNext(s -> System.out.println("doOnNext: " + s))
			.subscribe(System.out::println);*/
		/**
		 * doOnRequest:
		 * doOnRequest 操作符在request时执行指定的操作。
		 */
		/*Flux.just(1, 2, 3)
        	.doOnRequest(s -> System.out.println("doOnRequest: " + s))
        	.subscribe(System.out::println);*/
		/**
		 * doOnSubscribe:
		 * doOnSubscribe 操作符在subscribe时执行指定的操作。
		 */
		/*Flux.just(1, 2, 3)
        	.doOnSubscribe(s -> System.out.println("doOnSubscribe: " + s))
        	.subscribe(System.out::println);*/
		/**
		 * doOnComplete:
		 * doOnComplete 操作符在流中的所有元素发出完成时执行指定的操作。
		 */
		/*Flux.just(1, 2, 3)
        	.doOnComplete(() -> System.out.println("doOnComplete"))
        	.subscribe(System.out::println);*/
		/**
		 * doOnError
		 * doOnError 操作符可以在流中的每个元素发出错误时执行指定的操作。
		 */
		/*Flux.just(1, 2, 3)
        	.doOnError(e -> System.out.println("doOnError: " + e.getMessage()))
        	.subscribe(System.out::println);*/
		/**
		 * doOnTerminate
		 * doOnTerminate 操作符在流中的所有元素发出完成或错误时执行指定的操作。
		 */
		/*Flux.just(1, 2, 3)
        	.doOnTerminate(() -> System.out.println("doOnTerminate"))
        	.subscribe(System.out::println);*/
		/**
		 * doOnCancel:
		 * doOnCancel 操作符在流中的所有元素发出取消时执行指定的操作。
		 */
		/*Flux.just(1, 2, 3)
        	.doOnCancel(() -> System.out.println("doOnCancel"))
        	.subscribe(System.out::println);*/
		/**
		 * doOnDiscard
		 * doOnDiscard 操作符在流中的所有元素被丢弃时执行指定的操作。
		 */
		/*Flux.just(1, 2, 3)
        	.doOnDiscard(Object.class, s -> System.out.println("doOnDiscard: " + s))
        	.subscribe(System.out::println);*/
		/**
		 * 其他操作
		 */
		/**
		 * handle
		 * handle方法有些不同，它在 Mono 和 Flux 中都有。然而，它是一个实例方法 （instance method），意思就是它要链接在一个现有的源后使用（与其他操作符一样）。 handle 方法接收一个 BiConsumer，它的第一个参数是 sink，第二个参数是 Context。 它允许你对每个生成的元素做一些额外的处理，比如过滤、映射、分组等等。它也可以用于 push 或 pull 模式。
		 */
		/*Flux<String> alphabet=Flux.just(-1,30,13,9,20)
		        				  .handle((i,sink)->{
		        					  	String letter=alphabet(i);
		        					  	if(letter!=null)
		        					  		sink.next(letter);
		        				  });*/
	}
}
