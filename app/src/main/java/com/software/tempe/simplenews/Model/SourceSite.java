package com.software.tempe.simplenews.Model;

import java.util.List;

public class SourceSite {
    private String status;
    private int totalResult;
    private List<Sources> sources;

    public SourceSite() {

    }

    public SourceSite(String status, int totalResult, List<Sources> sources) {
        this.status = status;
        this.totalResult = totalResult;
        this.sources = sources;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public List<Sources> getSources() {
        return sources;
    }

    public void setSources(List<Sources> sources) {
        this.sources = sources;
    }
}
