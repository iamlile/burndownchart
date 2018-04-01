package com.iamlile.jira.burndown.vo;

/**
 * Created by macpro on 2018/3/22.
 */
public class WorkingDayPlan {
    private String date;
    private boolean isWorkingDay;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isWorkingDay() {
        return isWorkingDay;
    }

    public void setWorkingDay(boolean workingDay) {
        isWorkingDay = workingDay;
    }
}
