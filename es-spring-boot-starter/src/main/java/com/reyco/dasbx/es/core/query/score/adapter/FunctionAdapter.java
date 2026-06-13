package com.reyco.dasbx.es.core.query.score.adapter;

import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;

import com.reyco.dasbx.es.core.query.score.ScoreFunction;

public interface FunctionAdapter<T extends ScoreFunction> {

    /**
     * 支持类型
     */
    Class<T> support();

    /**
     * 转ES function
     */
    ScoreFunctionBuilder<?> adapt(T function);

}
