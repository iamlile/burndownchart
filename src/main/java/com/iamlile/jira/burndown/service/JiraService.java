package com.iamlile.jira.burndown.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iamlile.jira.burndown.mapper.JiraMapper;
import com.iamlile.jira.burndown.mapper.JiraSprintMapper;
import com.iamlile.jira.burndown.mapper.JiraSprintdailyDataMapper;
import com.iamlile.jira.burndown.model.*;
import com.iamlile.jira.burndown.util.BurnChartUtil;
import com.iamlile.jira.burndown.util.JiraHttpClientUtil;
import com.iamlile.jira.burndown.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Math.round;

/**
 * Created by lee on 2018/2/16.
 *
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
        return jiraSprintdailyDataMapper.getBySprintId(sprintId);
    }

    public void setJiraSprint(Integer jiraId) {
        JiraKey jiraKey = new JiraKey();
        jiraKey.setId(jiraId);
        JiraWithBLOBs jira = jiraMapper.selectByPrimaryKey(jiraKey);
        String username = jira.getUsername();
        String pass = jira.getPassword();
        String board_str = jira.getBoardWatch();
        List<String> boardList = Arrays.asList(board_str.replaceAll("\\[|\\]|\\\"", "").split(","));
        logger.info("遍历jira:{}-下的所有Board,", jiraId);
        for (String boardId : boardList) {
            logger.info("遍历Board:{}-下的所有Sprint,", boardId);
            List<JiraSprintWithBLOBs> sprints = getBoardSprintFromJira(username, pass, boardId);
            for (JiraSprintWithBLOBs sprint : sprints) {
                //入库
                sprint.setJiraId(jiraId);
                logger.info("写入数据库,sprintID:{}", sprint.getSprintIdInJira());
                // jiraSprintMapper.insert(sprint);
            }
        }


        //getSprintIssues(username,pass,13+"",49);
    }

    //每天定时写入sprint数据到数据库
    public void addSprintDailyData(Integer jiraId) {
        JiraKey jiraKey = new JiraKey();
        jiraKey.setId(jiraId);
        JiraWithBLOBs jira = jiraMapper.selectByPrimaryKey(jiraKey);
        String username = jira.getUsername();
        String pass = jira.getPassword();
        String board_str = jira.getBoardWatch();
        List<String> boardList = Arrays.asList(board_str.replaceAll("\\[|\\]|\\\"", "").split(","));
        logger.info("遍历jira:{}-下的所有Board,", jiraId);
        for (String boardId : boardList) {
            logger.info("遍历Board:{}-下的所有Sprint,", boardId);
            List<JiraSprintdailyDataWithBLOBs> data = this.getSprintDailyDataFromJira(username, pass, boardId);
            for (JiraSprintdailyDataWithBLOBs item : data) {
                //入库
                logger.info("写入数据库表:jira_sprintdailydata,sprintID:{},完成工时:{},剩余工时:{}"
                        , item.getSprintId(), item.getCompletedWorkHours(), item.getRemainingWorkHours());

                //jiraSprintdailyDataMapper.insert(item);
            }
        }
    }

    private List<JiraSprintdailyDataWithBLOBs> getSprintDailyDataFromJira(String username, String pass, String boardId) {
        List<JiraSprintdailyDataWithBLOBs> jiraSprintdailyDatas = new ArrayList<JiraSprintdailyDataWithBLOBs>();
        String path = JiraHttpClientUtil.PM_HOST + JiraHttpClientUtil.JIRA_SPRINTS_URL;
        path = path.replaceAll("board_id", boardId);
        String result = JiraHttpClientUtil.getJiraBoardsFromRemote(path, username, pass);
        logger.info("sprints from jira server : {}", result);
        Gson gson = new Gson();
        SprintPage page = gson.fromJson(result, SprintPage.class);
        List<Sprint> sprints = page.getValues();
        for (Sprint sprint : sprints) {

            logger.info("sprint:{}--,status:{}", sprint.getId(), sprint.getState());
            if ("active".equals(sprint.getState())) {
                //TODO:封装为JiraSprintdailyData
                logger.info("----------这个sprint是active的--{}------------");
                JiraSprintdailyDataWithBLOBs jiraSprintdailyData = new JiraSprintdailyDataWithBLOBs();
                Map<String, List> issues = getSprintIssuesFromJira(username, pass, boardId, sprint.getId());
                List<WorkerLog> workerLogs = issues.get("workerLogs");
                jiraSprintdailyData.setSprintId(sprint.getId());
                List<SprintTimeInfo> timeInfos = issues.get("timeInfos");
                SprintTimeInfo sprintTimeInfo = timeInfos.get(0);
                jiraSprintdailyData.setCompletedWorkHours(sprintTimeInfo.getFinishHours());
                jiraSprintdailyData.setRemainingWorkHours(sprintTimeInfo.getUnFinishHours());

                /**
                 update jira_sprintdailydata set remaining_work_hours=200,completed_work_hours=76 where id =127;
                 update jira_sprintdailydata set remaining_work_hours=169.5,completed_work_hours=106.5 where id =130;
                 update jira_sprintdailydata set remaining_work_hours=169.5,completed_work_hours=106.5 where id =133;
                 update jira_sprintdailydata set remaining_work_hours=139.5,completed_work_hours=136.5 where id = 136;
                 update jira_sprintdailydata set remaining_work_hours=119.5,completed_work_hours=156.5 where id = 139;
                 */
                /**仅用于测试
                 Format f = new SimpleDateFormat("yyyy-MM-dd");
                 Date today = new Date();
                 System.out.println("今天是:" + f.format(today));
                 Calendar c = Calendar.getInstance();
                 c.setTime(today);
                 c.add(Calendar.DAY_OF_MONTH, 2);// 今天+1天
                 jiraSprintdailyData.setDate(c.getTime());
                 **/
                jiraSprintdailyData.setDate(new Date());
                jiraSprintdailyData.setWorkerLog(gson.toJson(workerLogs));
                jiraSprintdailyDatas.add(jiraSprintdailyData);
            }

        }
        return jiraSprintdailyDatas;
    }


    private List<JiraSprintWithBLOBs> getBoardSprintFromJira(String username, String pass, String boardId) {
        logger.info("获取每个board下面的sprint. boardId:{}", boardId);
        List<JiraSprintWithBLOBs> jiraWithBLOBses = new ArrayList<JiraSprintWithBLOBs>();
        String path = JiraHttpClientUtil.PM_HOST + JiraHttpClientUtil.JIRA_SPRINTS_URL;
        path = path.replaceAll("board_id", boardId);
        String result = JiraHttpClientUtil.getJiraBoardsFromRemote(path, username, pass);
        Gson gson = new Gson();
        SprintPage page = gson.fromJson(result, SprintPage.class);
        List<Sprint> sprints = page.getValues();
        for (Sprint sprint : sprints) {

            logger.info("sprint:{}--,status:{}", sprint.getId(), sprint.getState());
            if ("active".equals(sprint.getState())) {
                logger.info("----------这个sprint是active的------------");
                //TODO:封装为JiraWithBLOBs
                JiraSprintWithBLOBs jiraSprintWithBLOBs = new JiraSprintWithBLOBs();
                jiraSprintWithBLOBs.setName(sprint.getName());
                jiraSprintWithBLOBs.setBoardId(Integer.parseInt(boardId));
                jiraSprintWithBLOBs.setSprintIdInJira(sprint.getId());
                jiraSprintWithBLOBs.setStartDate(sprint.getStartDate() == null ? "2011-01-02" : sprint.getStartDate().substring(0, 10));
                jiraSprintWithBLOBs.setCompleteDate(sprint.getCompleteDate() == null ? "2011-01-02" : sprint.getCompleteDate().substring(0, 10));
                jiraSprintWithBLOBs.setEndDate(sprint.getEndDate() == null ? "2011-01-02" : sprint.getEndDate().substring(0, 10));
                String workingDayPlan = gson.toJson(BurnChartUtil
                        .initWorkingDayPlan(sprint.getStartDate(), sprint.getEndDate()));
                jiraSprintWithBLOBs.setWorkingDayPlan(workingDayPlan);
                Map<String, List> issues = getSprintIssuesFromJira(username, pass, boardId, sprint.getId());
                List<NormalIssue> normalIssues = issues.get("normalIssues");
                List<CrossVersionIssue> crossVersionIssues = issues.get("crossIssues");
                jiraSprintWithBLOBs.setIssues(gson.toJson(normalIssues));
                jiraSprintWithBLOBs.setCrossVerIssues(gson.toJson(crossVersionIssues));
                jiraWithBLOBses.add(jiraSprintWithBLOBs);
            }

        }
        return jiraWithBLOBses;
    }


    public Map<String, List> getSprintIssuesFromJira(String username, String pass, String boardId, Integer sprintId) {
        logger.info("获取每个sprint下的Issue. boardId:{},sprintId:{}", boardId, sprintId);
        Map<String, List> issueToDbMap = new HashMap();
        List<NormalIssue> issues = new ArrayList<NormalIssue>();
        List<CrossVersionIssue> crossIssues = new ArrayList<CrossVersionIssue>();
        List<WorkerLog> workerLogs = new ArrayList<WorkerLog>();
        List<SprintTimeInfo> timeInfos = new ArrayList<SprintTimeInfo>();
        String path = JiraHttpClientUtil.PM_HOST + JiraHttpClientUtil.JIRA_ISSUE_URL;
        path = path.replaceAll("board_id", boardId.toString())
                .replaceAll("sprint_id", sprintId.toString());

        String result = JiraHttpClientUtil.getJiraBoardsFromRemote(path, username, pass);
        Gson gson = new Gson();
        Map map = gson.fromJson(result, Map.class);
        List<Map> issueListFromJira = (List) map.get("issues");
        SprintTimeInfo sprintTimeInfo = new SprintTimeInfo();
        Map<String, WorkerLog> tempWorkerlogMap = new HashMap<String, WorkerLog>();
        for (Map issueMap : issueListFromJira) {
            Map fieldsMap = (Map) issueMap.get("fields");
            Issue issue = gson.fromJson(gson.toJson(fieldsMap), Issue.class);

            issue.setId((String) issueMap.get("id"));
            issue.setKey((String) issueMap.get("key"));

            //判断是否为subtask
            issue.getIssuetype().isSubtask();
            issue.getSubtasks();
            //不含子任务的父任务 和 子任务
            if ((!issue.getIssuetype().isSubtask() && issue.getSubtasks() == null)
                    || issue.getIssuetype().isSubtask() && issue.getCustomfield_10200() != null) {
                String finishPercentStr = (String) issue.getCustomfield_10200().get("value");
                if (issue.getClosedSprints() == null) {//新任务
                    logger.info("{}-统计有效任务(new):{}-{}-{}-{}", sprintId, issue.getId(), issue.getKey(), issue.getSummary(), issue.getAssignee() == null ? "无Assignee" : issue.getAssignee().getDisplayName());
                    //TODO:存到普通任务列表
                    //  logger.info("统计新任务,完成百分比:{}",finishPercentStr);
                    NormalIssue normalIssue = new NormalIssue();
                    normalIssue.setId(issue.getId());
                    NormalIssueItem issueItem = new NormalIssueItem();
                    issueItem.setId(issue.getId());
                    issueItem.setName(issue.getSummary());
                    issueItem.setUrl(null);
                    issueItem.setEst_sec(issue.getTimetracking().getOriginalEstimateSeconds());
                    normalIssue.setNormalIssueItem(issueItem);
                    issues.add(normalIssue);

                    // sprintTimeInfo = sumOriginalEstimateHours(sprintTimeInfo,issue);
                } else {//从旧sprint迁移过来的旧任务
                   /* logger.info("从旧sprint迁移过来的旧任务个数:{}--{}--状态-{}",
                            issue.getClosedSprints().size(),finishPercentStr,issue.getStatus().getStatusCategory().getKey());
*/
                    if (!"100%".equals(finishPercentStr)
                            && !"done".equals(issue.getStatus().getStatusCategory().getKey())) {
                        logger.info("{}-统计有效任务(old):{}-{}-{}-{}", sprintId, issue.getId(), issue.getKey(), issue.getSummary(), issue.getAssignee() == null ? "无Assignee" : issue.getAssignee().getDisplayName());
                        // logger.info("统计没有完成的旧任务:{}");
                        //普通任务列表
                        NormalIssue normalIssue = new NormalIssue();
                        normalIssue.setId(issue.getId());
                        NormalIssueItem issueItem = new NormalIssueItem();
                        issueItem.setId(issue.getId());
                        issueItem.setName(issue.getSummary());
                        issueItem.setUrl(null);
                        issueItem.setEst_sec(issue.getTimetracking().getOriginalEstimateSeconds());
                        normalIssue.setNormalIssueItem(issueItem);
                        issues.add(normalIssue);
                        //跨任务列表
                        CrossVersionIssue crossVersionIssue = new CrossVersionIssue();
                        crossVersionIssue.setId(issue.getId());
                        CrossVersionIssueItem crossVersionIssueItem = new CrossVersionIssueItem();
                        crossVersionIssueItem.setEst(issue.getTimetracking().getOriginalEstimateSeconds());
                        crossVersionIssueItem.setPercent(finishPercentStr);
                        crossVersionIssue.setIssueItem(crossVersionIssueItem);
                        crossIssues.add(crossVersionIssue);

                    }

                }
                //工作日志workerlog列表
                WorkerLog workerLog = new WorkerLog();
                if (issue.getAssignee() != null) {
                    workerLog.setName(issue.getAssignee().getDisplayName());
                    //统计worker工时完成时间
                    int finishPercent = 0;
                    if (finishPercentStr != null && !"".equals(finishPercentStr)) {
                        finishPercentStr = finishPercentStr.replace("%", "");
                        finishPercent = Integer.parseInt(finishPercentStr);
                    }

                    if (issue.getTimetracking() != null) {
                        Long time = issue.getTimetracking().getOriginalEstimateSeconds();
                        // logger.info("任务:{} 原始估算工时:{}- ,完成百分比:{}-,完成工时:{}",issue.getKey(),time,finishPercent,((double)time / 3600 * finishPercent)/100);
                        if (time != null) {
                            Double workTime = ((double) time / 3600 * finishPercent) / 100;
                            if (tempWorkerlogMap.get(workerLog.getName()) != null) {

                                Double originalWorkTime = tempWorkerlogMap.get(workerLog.getName()).getCompletedHours();
                                if (originalWorkTime == null) originalWorkTime = 0D;
                                workerLog.setCompletedHours(workTime + originalWorkTime);
                                logger.info("-------已经存在--{}--的工作记录,新增加工时{},原来累计工时:{}", workerLog.getName(), workTime, originalWorkTime);
                            } else {
                                //DecimalFormat form = new DecimalFormat("#.00");//保留两位小数
                                logger.info("-------新的--{}--的工作记录", workerLog.getName());
                                workerLog.setCompletedHours(workTime);
                            }

                            logger.info("worker:{},完成工时:{}", workerLog.getName(), workerLog.getCompletedHours());
                        }
                    }
                }
                ;
                tempWorkerlogMap.put(workerLog.getName(), workerLog);
                sprintTimeInfo = sumOriginalEstimateHours(sprintTimeInfo, issue);
            }
        }
        issueToDbMap.put("normalIssues", issues);
        issueToDbMap.put("crossIssues", crossIssues);

        Iterator<WorkerLog> tempIterator = tempWorkerlogMap.values().iterator();
        while (tempIterator.hasNext()) {
            workerLogs.add(tempIterator.next());
        }
        issueToDbMap.put("workerLogs", workerLogs);
        timeInfos.add(sprintTimeInfo);
        issueToDbMap.put("timeInfos", timeInfos);
        logger.info("迭代版本:{},总估算工时:{}小时,总完成工时:{}小时,剩余工时:{}小时"
                , sprintId, sprintTimeInfo.getEstimateHours()
                , sprintTimeInfo.getFinishHours()
                , sprintTimeInfo.getUnFinishHours());
        return issueToDbMap;
    }

    private Double getEstimateHoursByBoadIdAndSprintId(String username, String pass, String boardId, Integer sprintId) {
        Map<String, List> issuesMap = this.getSprintIssuesFromJira(username, pass, boardId, sprintId);
        List<SprintTimeInfo> timeInfos = issuesMap.get("timeInfos");
        SprintTimeInfo sprintTimeInfo = timeInfos.get(0);
        return sprintTimeInfo.getEstimateHours();
    }

    public BurnDownChartData getBurnChartData(Integer boardId, Integer sprintId) {
        Double amount = 0D;
        BurnDownChartData burnDownChartData = new BurnDownChartData();
        List<Double> completeHour = new ArrayList<Double>();
        List<Double> remainHour = new ArrayList<Double>();
        Map<String, List> workerLogs = new LinkedHashMap<String, List>();
        List<JiraSprintdailyDataWithBLOBs> jiraSprintdaily = jiraSprintdailyDataMapper.getBySprintId(sprintId);
        Gson gson = new Gson();
        int count = 0;
        Type type = new TypeToken<List<WorkerLog>>() {
        }.getType();
        for (JiraSprintdailyDataWithBLOBs item : jiraSprintdaily) {
            if (count++ < 1) {
                amount = item.getCompletedWorkHours() + item.getRemainingWorkHours();
            }
            completeHour.add(item.getCompletedWorkHours());
            remainHour.add(item.getRemainingWorkHours());

            workerLogs.put(BurnChartUtil.formDateToStr(item.getDate()), gson.fromJson(item.getWorkerLog(), type));

        }
        burnDownChartData.setCompleteHour(completeHour);
        burnDownChartData.setRemainHour(remainHour);
        burnDownChartData.setWorkerLogs(workerLogs);

        JiraSprintWithBLOBs jiraSprintWithBLOBs = jiraSprintMapper.getJiraSprint(sprintId);

        type = new TypeToken<List<WorkingDayPlan>>() {
        }.getType();
        List<WorkingDayPlan> workingDayPlans = gson.fromJson(jiraSprintWithBLOBs.getWorkingDayPlan(), type);
        //生成燃尽图基准线的点
        List guideline = new ArrayList<>();
        List<String> xAisxData = new ArrayList<String>();
        int totalWorkingdays = BurnChartUtil.countWorkingDay(workingDayPlans);
        int dayNum = 0;//如果sprint周期内的第一天是开sprint会议(即不会消耗工时)，则day_num设置为0，否则设置为1；
        for (WorkingDayPlan plan : workingDayPlans) {
            xAisxData.add(plan.getDate());
            if (plan.isWorkingDay()) {
                //GUIDELINE = "{total}-({total}/float({days})*{num})";
                long guidelinePoint = round(amount - (amount / (totalWorkingdays - 1) * dayNum));
                guideline.add(guidelinePoint);
                dayNum++;
            } else {
                if (guideline.size() > 0) {
                    guideline.add(guideline.get(guideline.size() - 1));
                } else {
                    guideline.add(amount);
                }
            }
        }

        burnDownChartData.setyAisxMax(round(amount));
        burnDownChartData.setxAisxData(xAisxData);
        burnDownChartData.setGuideline(guideline);
        return burnDownChartData;
    }


    private SprintTimeInfo sumOriginalEstimateHours(SprintTimeInfo sprintTimeInfo, Issue issue) {
        String finishPercentStr = (String) issue.getCustomfield_10200().get("value");
        int finishPercent = 0;
        if (finishPercentStr != null && !"".equals(finishPercentStr)) {
            finishPercentStr = finishPercentStr.replace("%", "");
            finishPercent = Integer.parseInt(finishPercentStr);
        }
        if (issue.getTimetracking() != null) {
            System.out.println(issue.getTimetracking().getOriginalEstimateSeconds());
            Double time = 0D;
            if (issue.getTimetracking().getOriginalEstimateSeconds() != null) {
                time = (double) issue.getTimetracking().getOriginalEstimateSeconds();
            }
            if (time != null) {
                sprintTimeInfo.setEstimateHours(sprintTimeInfo.getEstimateHours() + time / 3600);
                sprintTimeInfo.setFinishHours(sprintTimeInfo.getFinishHours() + (time / 3600 * finishPercent) / 100);

            }
            logger.info("issue-{} -的累计原始估算时间是:{}小时,累计完成工时:{}小时"
                    , issue.getId(), sprintTimeInfo.getEstimateHours(), sprintTimeInfo.getFinishHours());
        }
        return sprintTimeInfo;
    }


}

class SprintTimeInfo {
    private Double estimateHours = 0D;
    private Double finishHours = 0D;

    public Double getEstimateHours() {
        return estimateHours;
    }

    public void setEstimateHours(Double estimateHours) {
        this.estimateHours = estimateHours;
    }

    public Double getFinishHours() {
        return finishHours;
    }

    public void setFinishHours(Double finishHours) {
        this.finishHours = finishHours;
    }

    public Double getUnFinishHours() {
        return estimateHours - finishHours;
    }
}
