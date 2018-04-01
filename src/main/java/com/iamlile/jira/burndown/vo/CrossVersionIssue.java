package com.iamlile.jira.burndown.vo;

/**
 * Created by macpro on 2018/3/21.
 * 跨版本的Issue数据 对应数据字段:cross_ver_issues,格式: {"19366":{"est":28800,"percent":"100%"}},
 */
public class CrossVersionIssue {
    private String id;
    private CrossVersionIssueItem issueItem;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CrossVersionIssueItem getIssueItem() {
        return issueItem;
    }

    public void setIssueItem(CrossVersionIssueItem issueItem) {
        this.issueItem = issueItem;
    }
}

