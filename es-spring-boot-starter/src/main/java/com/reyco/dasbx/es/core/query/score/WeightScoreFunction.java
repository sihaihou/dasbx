package com.reyco.dasbx.es.core.query.score;

/**
 * 权重加分
 * @author reyco
 *
 */
public class WeightScoreFunction implements ScoreFunction {

    private Float weight;

    public WeightScoreFunction(
            Float weight){

        this.weight = weight;
    }

    public Float getWeight() {
        return weight;
    }
}
