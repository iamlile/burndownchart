package com.iamlile.jira.burndown.vo;

import java.util.List;
import java.util.Map;

/**
 * Created by macpro on 2018/3/22.
 */
public class BurnDownChartData {
    private List xAisxData;
    private Long yAisxMax;
    private List guideline;
    private List completeHour;
    private List remainHour;
    private Map<String, List> workerLogs;

    public List getxAisxData() {
        return xAisxData;
    }

    public void setxAisxData(List xAisxData) {
        this.xAisxData = xAisxData;
    }

    public Long getyAisxMax() {
        return yAisxMax;
    }

    public void setyAisxMax(Long yAisxMax) {
        this.yAisxMax = yAisxMax;
    }

    public List getGuideline() {
        return guideline;
    }

    public void setGuideline(List guideline) {
        this.guideline = guideline;
    }

    public List getCompleteHour() {
        return completeHour;
    }

    public void setCompleteHour(List completeHour) {
        this.completeHour = completeHour;
    }

    public List getRemainHour() {
        return remainHour;
    }

    public void setRemainHour(List remainHour) {
        this.remainHour = remainHour;
    }

    public Map<String, List> getWorkerLogs() {
        return workerLogs;
    }

    public void setWorkerLogs(Map<String, List> workerLogs) {
        this.workerLogs = workerLogs;
    }
}
