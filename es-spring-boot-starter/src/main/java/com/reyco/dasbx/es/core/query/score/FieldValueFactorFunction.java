package com.reyco.dasbx.es.core.query.score;

/**
 * 按字段加权
 * @author reyco
 *
 */
public class FieldValueFactorFunction implements ScoreFunction {

    private String field;

    private Float factor = 1F;

    public FieldValueFactorFunction(String field){
        this.field = field;
    }

    public FieldValueFactorFunction factor(Float factor){
        this.factor = factor;
        return this;
    }

    public String getField() {
        return field;
    }

    public Float getFactor() {
        return factor;
    }
}
