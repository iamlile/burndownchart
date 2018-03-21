package com.iamlile.jira.burndown.vo;

import java.util.List;

/**
 * Created by macpro on 2018/3/20.
 */
public class Sprint {
    private Integer id;
    private String self;
    private String state;
    private String name;
    private String startDate;
    private String endDate;
    private String completeDate;
    private Integer originBoardId;

    public List getIssues() {
        return issues;
    }

    public void setIssues(List issues) {
        this.issues = issues;
    }

    private List issues;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }

    public Integer getOriginBoardId() {
        return originBoardId;
    }

    public void setOriginBoardId(Integer originBoardId) {
        this.originBoardId = originBoardId;
    }
}
