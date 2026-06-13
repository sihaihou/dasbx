package com.reyco.dasbx.es.core.query.score;

public class RandomScoreFunction implements ScoreFunction {

    private Integer seed;

    public RandomScoreFunction(Integer seed){
        this.seed = seed;
    }

    public Integer getSeed() {
        return seed;
    }
}
