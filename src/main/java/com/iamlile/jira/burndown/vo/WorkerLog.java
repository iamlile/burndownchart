package com.iamlile.jira.burndown.vo;

/**
 * Created by macpro on 2018/3/22.
 */
public class WorkerLog {
    private String name;
    private Double completedHours;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCompletedHours() {
        return completedHours;
    }

    public void setCompletedHours(Double completedHours) {
        this.completedHours = completedHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkerLog workerLog = (WorkerLog) o;

        return name.equals(workerLog.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
