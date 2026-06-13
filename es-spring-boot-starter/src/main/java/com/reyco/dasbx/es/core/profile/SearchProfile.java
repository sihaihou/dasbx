package com.reyco.dasbx.es.core.profile;

import java.util.ArrayList;
import java.util.List;

public class SearchProfile {
	/**
     * 总耗时
     */
    private long totalTook;

    /**
     * 各阶段耗时
     */
    private final List<PipelineProfile> pipelines = new ArrayList<>();

    public long getTotalTook() {
        return totalTook;
    }

    public void setTotalTook(long totalTook) {
        this.totalTook = totalTook;
    }

    public List<PipelineProfile> getPipelines() {
        return pipelines;
    }

    public void addPipeline(PipelineProfile profile) {
        pipelines.add(profile);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Total Took: ")
          .append(totalTook)
          .append("ms\n\n");

        for (PipelineProfile pipeline : pipelines) {
            sb.append(pipeline)
              .append("\n");
        }

        return sb.toString();
    }
}
