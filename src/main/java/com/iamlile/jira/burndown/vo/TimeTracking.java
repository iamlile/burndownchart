package com.iamlile.jira.burndown.vo;

/**
 * https://wtanaka.com/node/8143
 * Submitted by wtanaka on Wed, 2015-02-04 20:52 (Tech
 * )
 * <p>
 * The JIRA API has (at least) four fields exposing time tracking data. In the jira-python python wrapper, these are exposed as follows, assuming you have a local variable issue containing an issue object.:
 * <p>
 * issue.fields.timeestimate
 * issue.fields.timeoriginalestimate
 * issue.fields.aggregatetimeestimate
 * issue.fields.aggregatetimeoriginalestimate
 * <p>
 * timeestimate and aggregatetimeestimate track the "Remaining" time left on the issue.
 * <p>
 * timeoriginalestimate and aggregatetimeoriginalestimate track the original "Estimated" value.
 * <p>
 * Although I have not tested it, I suspect that the aggregate variables contain the sum of all subtask estimates. For normal tasks, they seem to always equal the non-aggregate fields.
 */
public class TimeTracking {
    private Long originalEstimateSeconds;
    private Long remainingEstimateSeconds;

    public Long getOriginalEstimateSeconds() {
        return originalEstimateSeconds;
    }

    public void setOriginalEstimateSeconds(Long originalEstimateSeconds) {
        this.originalEstimateSeconds = originalEstimateSeconds;
    }

    public Long getRemainingEstimateSeconds() {
        return remainingEstimateSeconds;
    }

    public void setRemainingEstimateSeconds(Long remainingEstimateSeconds) {
        this.remainingEstimateSeconds = remainingEstimateSeconds;
    }
}
