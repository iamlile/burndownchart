package com.iamlile.jira.burndown.vo;

import java.util.List;
import java.util.Map;

/**
 * Created by macpro on 2018/3/21.
 */
public class Issue {
    private String id;
    private String key;


    private String summary;
    private Long aggregatetimeestimate;//track the "Remaining" time left on the issue
    private TimeTracking timetracking;
    private Assignee assignee;
    private Issuetype issuetype;
    private List subtasks;
    private Map customfield_10200;//TASK_PRECENT
    private Object customfield_10005;

    public Object getCustomfield_10005() {
        return customfield_10005;
    }

    public void setCustomfield_10005(Object customfield_10005) {
        this.customfield_10005 = customfield_10005;
    }

    private Status status;
    private List closedSprints;

    public TimeTracking getTimetracking() {
        return timetracking;
    }

    public void setTimetracking(TimeTracking timetracking) {
        this.timetracking = timetracking;
    }

    public Assignee getAssignee() {
        return assignee;
    }

    public void setAssignee(Assignee assignee) {
        this.assignee = assignee;
    }

    public Issuetype getIssuetype() {
        return issuetype;
    }

    public void setIssuetype(Issuetype issuetype) {
        this.issuetype = issuetype;
    }

    public List getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List subtasks) {
        this.subtasks = subtasks;
    }

    public Map getCustomfield_10200() {
        return customfield_10200;
    }

    public void setCustomfield_10200(Map customfield_10200) {
        this.customfield_10200 = customfield_10200;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List getClosedSprints() {
        return closedSprints;
    }

    public void setClosedSprints(List closedSprints) {
        this.closedSprints = closedSprints;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Long getAggregatetimeestimate() {
        return aggregatetimeestimate;
    }

    public void setAggregatetimeestimate(Long aggregatetimeestimate) {
        this.aggregatetimeestimate = aggregatetimeestimate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

