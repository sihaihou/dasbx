package com.reyco.dasbx.es.core.explain;

public class SearchExplain {

	/**
     * 用户构建的原始Query
     */
    private String originalQuery;

    /**
     * 最终发送给ES的DSL
     */
    private String finalDsl;

    public String getOriginalQuery() {
        return originalQuery;
    }

    public void setOriginalQuery(String originalQuery) {
        this.originalQuery = originalQuery;
    }

    public String getFinalDsl() {
        return finalDsl;
    }

    public void setFinalDsl(String finalDsl) {
        this.finalDsl = finalDsl;
    }

    @Override
    public String toString() {
        return String.format(
                "============== Search Explain ==============\n\n"
                        + "Original Query\n"
                        + "--------------------------------\n%s\n\n"
                        + "Final DSL\n"
                        + "--------------------------------\n%s\n",
                originalQuery,
                finalDsl
        );
    }
}
