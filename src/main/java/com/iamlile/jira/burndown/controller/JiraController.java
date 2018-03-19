package com.iamlile.jira.burndown.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.iamlile.jira.burndown.mapper.JiraMapper;
import com.iamlile.jira.burndown.model.*;
import com.iamlile.jira.burndown.service.JiraService;
import com.iamlile.jira.burndown.util.JiraHttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by lee on 2018/2/16.
 */

@RestController
@RequestMapping("/api/jira")
public class JiraController {
    final static Logger logger = LoggerFactory.getLogger(JiraController.class);


    @Autowired
    JiraService jiraService;

    @Autowired
    JiraMapper jiraMapper;

    @RequestMapping(value = "/{jiraId}/boards", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getAllBoards(@PathVariable Integer jiraId) {
        JiraKey jiraKey = new JiraKey();
        jiraKey.setId(jiraId);
        JiraWithBLOBs jira = jiraMapper.selectByPrimaryKey(jiraKey);
        String url = jira.getUrl() + JiraHttpClientUtil.JIRA_BOARDS_URL;
        String result_json = JiraHttpClientUtil.getJiraBoardsFromRemote(url, jira.getUsername(), jira.getPassword());
        JsonElement je = new JsonParser().parse(result_json);
        String values = je.getAsJsonObject().get("values").toString();
        logger.debug("getAllBoards服务器的响应是:" + values);
        logger.debug(result_json);
        return values;
    }

    @RequestMapping(value = "/{jiraId}/boards/{boardId}/sprints/", method = RequestMethod.GET)
    public List<JiraSprint> getSprintsByBoardId(@PathVariable Integer jiraId, @PathVariable Integer boardId) {
        List<JiraSprint> list = jiraService.getJiraSprintList(jiraId, boardId);
        return list;
    }

    @RequestMapping(value = "/remote/{jiraId}/boards/{boardId}/sprints/", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String getRemoteSprintsByBoardId(@PathVariable Integer jiraId, @PathVariable Integer boardId) {
        String result_json = jiraService.getRemoteJiraSprintsByBoardId(jiraId,boardId);
        return result_json;
    }

    @RequestMapping(value = "/{jiraId}/boards/{boardId}/sprints/{sprintId}/burndown_chart/", method = RequestMethod.GET)
    public List<JiraSprintdailyDataWithBLOBs> getSprintsDailyDataBySpringId(@PathVariable Integer jiraId, @PathVariable Integer boardId, @PathVariable Integer sprintId) {
        List<JiraSprintdailyDataWithBLOBs> list = jiraService.getJiraSprintdailyDataList(sprintId);
        return list;
    }



}
