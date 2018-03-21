package com.iamlile.jira.burndown.vo;

import java.util.List;

/**
 * Created by macpro on 2018/3/20.
 */
public class SprintPage {
    private Integer maxResults;
    private Integer startAt;
    private boolean isLast;
    private List<Sprint> values;

    public Integer getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public Integer getStartAt() {
        return startAt;
    }

    public void setStartAt(Integer startAt) {
        this.startAt = startAt;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public List<Sprint> getValues() {
        return values;
    }

    public void setValues(List<Sprint> values) {
        this.values = values;
    }
}
