package com.iamlile.jira.burndown.vo;

public class CrossVersionIssueItem {
    private Long est;//估算时间
    private String percent;//完成百分比

    public Long getEst() {
        return est;
    }

    public void setEst(Long est) {
        this.est = est;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
