package com.iamlile.jira.burndown.service;

import com.iamlile.jira.burndown.mapper.JiraMapper;
import com.iamlile.jira.burndown.mapper.JiraSprintMapper;
import com.iamlile.jira.burndown.mapper.JiraSprintdailyDataMapper;
import com.iamlile.jira.burndown.model.*;
import com.iamlile.jira.burndown.util.JiraHttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lee on 2018/2/16.
 * @author lee
 */
@Service
public class JiraService {
    final static Logger logger = LoggerFactory.getLogger(JiraService.class);

    @Autowired
    JiraSprintMapper jiraSprintMapper;

    @Autowired
    JiraMapper jiraMapper;

    @Autowired
    JiraSprintdailyDataMapper jiraSprintdailyDataMapper;


    public List<JiraSprint> getJiraSprintList(Integer jira_id, Integer board_id) {
        return jiraSprintMapper.getJiraSprintList(jira_id, board_id);
    }

    public String getRemoteJiraSprintsByBoardId(Integer jiraId, Integer boardId) {
        JiraKey jiraKey = new JiraKey();
        jiraKey.setId(jiraId);
        JiraWithBLOBs jira = jiraMapper.selectByPrimaryKey(jiraKey);
        String url = jira.getUrl() + JiraHttpClientUtil.JIRA_SPRINTS_URL;
        url = url.replaceAll("board_id", boardId.toString());
        String result_json = JiraHttpClientUtil.getJiraBoardsFromRemote(url, jira.getUsername(), jira.getPassword());
        logger.debug(result_json);
        return result_json;
    }

    public List<JiraSprintdailyDataWithBLOBs> getJiraSprintdailyDataList(Integer sprintId) {
        return  jiraSprintdailyDataMapper.getBySprintId(sprintId);
    }

    public void setJiraSprint(Integer jiraId){

        JiraKey jiraKey = new JiraKey();
        jiraKey.setId(jiraId);
        JiraWithBLOBs jira = jiraMapper.selectByPrimaryKey(jiraKey);
        String username = jira.getUsername();
        String pass = jira.getPassword();
        String board_str = jira.getBoardWatch();
        List<String> boardList = Arrays.asList(board_str.replaceAll("\\[|\\]|\\\"","").split(","));
        for(String boardId:boardList){
            System.out.println(boardId);
            String path = "http://pm.igeeker.org/" + JiraHttpClientUtil.JIRA_SPRINTS_URL;
            path = path.replaceAll("board_id",boardId);
            System.out.println(path);
            String result = JiraHttpClientUtil.getJiraBoardsFromRemote(path, username, pass);
            System.out.println("----------");
            System.out.println(result);
        }


    }

}
