package com.reyco.dasbx.es.core.log;

import org.elasticsearch.action.search.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.reyco.dasbx.es.core.query.SearchContext;

public class DefaultDslLogger implements DslLogger {
	
	private static final Logger log = LoggerFactory.getLogger(DefaultDslLogger.class);

    @Override
    public void log(SearchContext context,long took) {
        SearchResponse response = context.getResponse();

        long total = 0;
        if (response != null
                && response.getHits() != null
                && response.getHits().getTotalHits() != null) {
            total = response.getHits().getTotalHits().value;
        }

        if (log.isDebugEnabled()) {
            log.debug(
                    "\n========== Elasticsearch DSL ==========\n" +
                    "index     : {}\n" +
                    "cost      : {} ms\n" +
                    "totalHits : {}\n" +
                    "dsl       : {}\n" +
                    "=======================================\n",
                    context.getIndex(),
                    took,
                    total,
                    prettyDsl(context)
            );
        }

        if (took > 300) {
            log.warn("Slow Elasticsearch Query : {} ms , dsl : {}",took,context.getSourceBuilder()
            );
        }
    }

    @Override
    public void error(SearchContext context,long took,Exception e) {
        log.error(
                "\n========== Elasticsearch ERROR ==========\n" +
                "index   : {}\n" +
                "cost    : {} ms\n" +
                "dsl     : {}\n" +
                "error   : {}\n" +
                "=========================================\n",
                context.getIndex(),
                took,
                prettyDsl(context),
                e.getMessage(),
                e
        );
    }

    private String prettyDsl(SearchContext context) {
        if (context.getSourceBuilder() == null) {
            return "";
        }
        try {
            Object obj = JSON.parse(context.getSourceBuilder().toString());
            return JSON.toJSONString(obj,SerializerFeature.PrettyFormat);
        } catch (Exception e) {
            return context.getSourceBuilder().toString();
        }
    }
}
