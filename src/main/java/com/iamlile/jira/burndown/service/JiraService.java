package com.iamlile.jira.burndown.service;

import com.google.gson.Gson;
import com.iamlile.jira.burndown.mapper.JiraMapper;
import com.iamlile.jira.burndown.mapper.JiraSprintMapper;
import com.iamlile.jira.burndown.mapper.JiraSprintdailyDataMapper;
import com.iamlile.jira.burndown.model.*;
import com.iamlile.jira.burndown.util.JiraHttpClientUtil;
import com.iamlile.jira.burndown.vo.Issue;
import com.iamlile.jira.burndown.vo.Sprint;
import com.iamlile.jira.burndown.vo.SprintPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
            List<Sprint> sprints = getBoardSprint(username, pass, boardId);
        }


        //getSprintIssues(username,pass,13+"",49);
    }

    public List<Sprint> getBoardSprint(String username, String pass, String boardId) {
        logger.info("获取每个board下面的sprint. boardId:{}",boardId);
        String path = "http://pm.igeeker.org/" + JiraHttpClientUtil.JIRA_SPRINTS_URL;
        path = path.replaceAll("board_id",boardId);
        String result = JiraHttpClientUtil.getJiraBoardsFromRemote(path, username, pass);
        Gson gson = new Gson();
        SprintPage page = gson.fromJson(result, SprintPage.class);
        List<Sprint> sprints = page.getValues();
        for(Sprint sprint : sprints){
            logger.info("sprint:{}--,status:{}",sprint.getId(),sprint.getState());
            if("active".equals(sprint.getState())) {
                logger.info("----------这个sprint是active的------------");
                List<Issue> issues = getSprintIssues(username, pass, boardId, sprint.getId());
                sprint.setIssues(issues);
            }
        }
        return sprints;
    }


    public List<Issue> getSprintIssues(String username,String pass,String boardId,Integer sprintId){
        logger.info("获取每个sprint下的Issue. boardId:{},sprintId:{}",boardId,sprintId);
        List<Issue> issues = new ArrayList<Issue>();
        String path = "http://pm.igeeker.org/" + JiraHttpClientUtil.JIRA_ISSUE_URL;
        path = path.replaceAll("board_id",boardId.toString())
                .replaceAll("sprint_id",sprintId.toString());

        String result = JiraHttpClientUtil.getJiraBoardsFromRemote(path, username, pass);
        Gson gson = new Gson();
        Map map = gson.fromJson(result,Map.class);
        List<Map> issueListFromJira = (List) map.get("issues");
        SprintTimeInfo sprintTimeInfo = new SprintTimeInfo();
        for(Map issueMap : issueListFromJira){
            Map fieldsMap = (Map) issueMap.get("fields");
            Issue issue = gson.fromJson(gson.toJson(fieldsMap), Issue.class);
            issues.add(issue);
            issue.setId((String)issueMap.get("id"));
            issue.setKey((String)issueMap.get("key"));

            //判断是否为subtask
            issue.getIssuetype().isSubtask();
            issue.getSubtasks();
            //不含子任务的父任务 和 子任务
            if((!issue.getIssuetype().isSubtask()&&issue.getSubtasks()==null)
                    || issue.getIssuetype().isSubtask() && issue.getCustomfield_10200()!=null){
                String finishPercentStr = (String)issue.getCustomfield_10200().get("value");
                if(issue.getClosedSprints() == null){//新任务
                    logger.info("统计新任务,完成百分比:{}",finishPercentStr);
                    //estimateHours = sumOriginalEstimateHours(estimateHours,finishHours, issue);
                    sprintTimeInfo = sumOriginalEstimateHours(sprintTimeInfo,issue);
                }else{//从旧sprint迁移过来的旧任务
                    logger.info("从旧sprint迁移过来的旧任务个数:{}--{}--状态-{}",
                            issue.getClosedSprints().size(),finishPercentStr,issue.getStatus().getStatusCategory().getKey());

                    if(!"100%".equals(finishPercentStr)
                            &&!"done".equals(issue.getStatus().getStatusCategory().getKey())){
                        logger.info("统计没有完成的旧任务:{}");
                        //TODO:这里的算法还不明确,待定
                      // estimateHours = sumOriginalEstimateHours(estimateHours,finishHours, issue);
                        sprintTimeInfo = sumOriginalEstimateHours(sprintTimeInfo,issue);
                    }
                }
            }
        }
        logger.info("迭代版本:{},总估算工时:{}小时,总完成工时:{}小时,剩余工时:{}小时"
                ,sprintId,sprintTimeInfo.getEstimateHours()
                ,sprintTimeInfo.getFinishHours()
                ,sprintTimeInfo.getUnFinishHours());
        return issues;
    }

    private SprintTimeInfo sumOriginalEstimateHours(SprintTimeInfo sprintTimeInfo, Issue issue) {
        String finishPercentStr = (String)issue.getCustomfield_10200().get("value");
        int finishPercent = 0;
        if(finishPercentStr!=null && !"".equals(finishPercentStr)){
            finishPercentStr = finishPercentStr.replace("%","");
            finishPercent = Integer.parseInt(finishPercentStr);
        }
        if(issue.getTimetracking()!=null) {
            Long time = issue.getTimetracking().getOriginalEstimateSeconds();
            if(time!= null) {
                sprintTimeInfo.setEstimateHours(sprintTimeInfo.getEstimateHours() + time / 3600);
                sprintTimeInfo.setFinishHours(sprintTimeInfo.getFinishHours() + (time / 3600 * finishPercent)/100);

            }
            logger.info("issue-{} -的累计原始估算时间是:{}小时,累计完成工时:{}小时"
                    , issue.getId(), sprintTimeInfo.getEstimateHours(),sprintTimeInfo.getFinishHours());
        }
        return sprintTimeInfo;
    }


}

class SprintTimeInfo{
    private Long estimateHours = 0L;
    private Long finishHours = 0L;


    public Long getEstimateHours() {
        return estimateHours;
    }

    public void setEstimateHours(Long estimateHours) {
        this.estimateHours = estimateHours;
    }

    public Long getFinishHours() {
        return finishHours;
    }

    public void setFinishHours(Long finishHours) {
        this.finishHours = finishHours;
    }

    public Long getUnFinishHours(){
        return estimateHours - finishHours;
    }
}
