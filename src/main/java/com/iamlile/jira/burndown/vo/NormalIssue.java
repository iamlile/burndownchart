package com.iamlile.jira.burndown.vo;

/**
 * Created by macpro on 2018/3/21.
 */
public class NormalIssue {
    private String id;
    private NormalIssueItem normalIssueItem;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NormalIssueItem getNormalIssueItem() {
        return normalIssueItem;
    }

    public void setNormalIssueItem(NormalIssueItem normalIssueItem) {
        this.normalIssueItem = normalIssueItem;
    }
}

