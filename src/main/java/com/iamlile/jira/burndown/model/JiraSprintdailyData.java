package com.iamlile.jira.burndown.model;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class JiraSprintdailyData extends JiraSprintdailyDataKey {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jira_sprintdailydata.date
     *
     * @mbg.generated Sun Mar 11 14:31:36 CST 2018
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date date;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jira_sprintdailydata.remaining_work_hours
     *
     * @mbg.generated Sun Mar 11 14:31:36 CST 2018
     */
    private Double remainingWorkHours;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jira_sprintdailydata.completed_work_hours
     *
     * @mbg.generated Sun Mar 11 14:31:36 CST 2018
     */
    private Double completedWorkHours;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column jira_sprintdailydata.sprint_id
     *
     * @mbg.generated Sun Mar 11 14:31:36 CST 2018
     */
    private Integer sprintId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jira_sprintdailydata.date
     *
     * @return the value of jira_sprintdailydata.date
     *
     * @mbg.generated Sun Mar 11 14:31:36 CST 2018
     */
    public Date getDate() {
        return date;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jira_sprintdailydata.date
     *
     * @param date the value for jira_sprintdailydata.date
     *
     * @mbg.generated Sun Mar 11 14:31:36 CST 2018
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jira_sprintdailydata.remaining_work_hours
     *
     * @return the value of jira_sprintdailydata.remaining_work_hours
     *
     * @mbg.generated Sun Mar 11 14:31:36 CST 2018
     */
    public Double getRemainingWorkHours() {
        return remainingWorkHours;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jira_sprintdailydata.remaining_work_hours
     *
     * @param remainingWorkHours the value for jira_sprintdailydata.remaining_work_hours
     *
     * @mbg.generated Sun Mar 11 14:31:36 CST 2018
     */
    public void setRemainingWorkHours(Double remainingWorkHours) {
        this.remainingWorkHours = remainingWorkHours;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jira_sprintdailydata.completed_work_hours
     *
     * @return the value of jira_sprintdailydata.completed_work_hours
     *
     * @mbg.generated Sun Mar 11 14:31:36 CST 2018
     */
    public Double getCompletedWorkHours() {
        return completedWorkHours;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jira_sprintdailydata.completed_work_hours
     *
     * @param completedWorkHours the value for jira_sprintdailydata.completed_work_hours
     *
     * @mbg.generated Sun Mar 11 14:31:36 CST 2018
     */
    public void setCompletedWorkHours(Double completedWorkHours) {
        this.completedWorkHours = completedWorkHours;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column jira_sprintdailydata.sprint_id
     *
     * @return the value of jira_sprintdailydata.sprint_id
     *
     * @mbg.generated Sun Mar 11 14:31:36 CST 2018
     */
    public Integer getSprintId() {
        return sprintId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column jira_sprintdailydata.sprint_id
     *
     * @param sprintId the value for jira_sprintdailydata.sprint_id
     *
     * @mbg.generated Sun Mar 11 14:31:36 CST 2018
     */
    public void setSprintId(Integer sprintId) {
        this.sprintId = sprintId;
    }
}