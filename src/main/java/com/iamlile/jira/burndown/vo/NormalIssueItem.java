package com.iamlile.jira.burndown.vo;

public class NormalIssueItem{
    private String id;
    private String name;
    private String url;
    private Long est_sec;//估算时间,单位秒

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getEst_sec() {
        return est_sec;
    }

    public void setEst_sec(Long est_sec) {
        this.est_sec = est_sec;
    }
}
