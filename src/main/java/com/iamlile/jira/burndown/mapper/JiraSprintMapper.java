package com.iamlile.jira.burndown.mapper;

import com.iamlile.jira.burndown.model.JiraSprint;
import com.iamlile.jira.burndown.model.JiraSprintKey;
import com.iamlile.jira.burndown.model.JiraSprintWithBLOBs;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface JiraSprintMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jira_sprint
     *
     * @mbg.generated Tue Mar 20 20:19:58 CST 2018
     */
    @Delete({
        "delete from jira_sprint",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(JiraSprintKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jira_sprint
     *
     * @mbg.generated Tue Mar 20 20:19:58 CST 2018
     */
    @Insert({
        "insert into jira_sprint (id, board_id, ",
        "sprint_id_in_jira, name, ",
        "jira_id, start_date, ",
        "end_date, complete_date, ",
        "working_day_plan, cross_ver_issues, ",
        "issues)",
        "values (#{id,jdbcType=INTEGER}, #{boardId,jdbcType=INTEGER}, ",
        "#{sprintIdInJira,jdbcType=INTEGER}, #{name,jdbcType=VARCHAR}, ",
        "#{jiraId,jdbcType=INTEGER}, #{startDate,jdbcType=DATE}, ",
        "#{endDate,jdbcType=DATE}, #{completeDate,jdbcType=DATE}, ",
        "#{workingDayPlan,jdbcType=LONGVARCHAR}, #{crossVerIssues,jdbcType=LONGVARCHAR}, ",
        "#{issues,jdbcType=LONGVARCHAR})"
    })
    int insert(JiraSprintWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jira_sprint
     *
     * @mbg.generated Tue Mar 20 20:19:58 CST 2018
     */
    int insertSelective(JiraSprintWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jira_sprint
     *
     * @mbg.generated Tue Mar 20 20:19:58 CST 2018
     */
    @Select({
        "select",
        "id, board_id, sprint_id_in_jira, name, jira_id, start_date, end_date, complete_date, ",
        "working_day_plan, cross_ver_issues, issues",
        "from jira_sprint",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @ResultMap("com.iamlile.jira.burndown.mapper.JiraSprintMapper.ResultMapWithBLOBs")
    JiraSprintWithBLOBs selectByPrimaryKey(JiraSprintKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jira_sprint
     *
     * @mbg.generated Tue Mar 20 20:19:58 CST 2018
     */
    int updateByPrimaryKeySelective(JiraSprintWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jira_sprint
     *
     * @mbg.generated Tue Mar 20 20:19:58 CST 2018
     */
    @Update({
        "update jira_sprint",
        "set board_id = #{boardId,jdbcType=INTEGER},",
          "sprint_id_in_jira = #{sprintIdInJira,jdbcType=INTEGER},",
          "name = #{name,jdbcType=VARCHAR},",
          "jira_id = #{jiraId,jdbcType=INTEGER},",
          "start_date = #{startDate,jdbcType=DATE},",
          "end_date = #{endDate,jdbcType=DATE},",
          "complete_date = #{completeDate,jdbcType=DATE},",
          "working_day_plan = #{workingDayPlan,jdbcType=LONGVARCHAR},",
          "cross_ver_issues = #{crossVerIssues,jdbcType=LONGVARCHAR},",
          "issues = #{issues,jdbcType=LONGVARCHAR}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKeyWithBLOBs(JiraSprintWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table jira_sprint
     *
     * @mbg.generated Tue Mar 20 20:19:58 CST 2018
     */
    @Update({
        "update jira_sprint",
        "set board_id = #{boardId,jdbcType=INTEGER},",
          "sprint_id_in_jira = #{sprintIdInJira,jdbcType=INTEGER},",
          "name = #{name,jdbcType=VARCHAR},",
          "jira_id = #{jiraId,jdbcType=INTEGER},",
          "start_date = #{startDate,jdbcType=DATE},",
          "end_date = #{endDate,jdbcType=DATE},",
          "complete_date = #{completeDate,jdbcType=DATE}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(JiraSprint record);

    @Select(value = "select id,board_id,sprint_id_in_jira,name,jira_id from jira_sprint where jira_id = #{jira_id} and board_id = #{board_id}")
    @Results(value = { @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER),
            @Result(column = "board_id", property = "boardId", jdbcType = JdbcType.INTEGER),
            @Result(column = "sprint_id_in_jira", property = "sprintIdInJira", jdbcType = JdbcType.INTEGER),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "jira_id", property = "jiraId", jdbcType = JdbcType.INTEGER),
            @Result(column = "start_date", property = "startDate", jdbcType = JdbcType.DATE),
            @Result(column = "end_date", property = "endDate", jdbcType = JdbcType.DATE),
            @Result(column = "complete_date", property = "endDate", jdbcType = JdbcType.DATE)
    })
    List<JiraSprint> getJiraSprintList(@Param("jira_id") Integer jira_id, @Param("board_id") Integer board_id);

}