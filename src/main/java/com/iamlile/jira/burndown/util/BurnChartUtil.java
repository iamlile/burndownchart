package com.iamlile.jira.burndown.util;

import com.iamlile.jira.burndown.vo.BurnDownChartData;
import com.iamlile.jira.burndown.vo.WorkingDayPlan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.Math.round;

/**
 * Created by macpro on 2018/3/22.
 * http://blog.csdn.net/qq_23477421/article/details/52230093
 */
public class BurnChartUtil {
    public static final String DATE_SEPRATOR = "-";
    public static final String DATE_FORMATE = "yyyy-MM-dd";

    public static String formDateToStr(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMATE);
        return formatter.format(date);
    }

    public static boolean isWeekend(String date) {
        String[] da = date.split(DATE_SEPRATOR);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.valueOf(da[0]));
        calendar.set(Calendar.MONTH, Integer.valueOf(da[1]) - 1);//月份比正常小1,0代表一月
        calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(da[2]));
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ||
                calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            return true;
        } else {
            return false;
        }
    }

    public static List initWorkingDayPlan(String startDateStr, String endDateStr){
        List list = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMATE);
        try {
            Date startDate = formatter.parse(startDateStr);
            Date endDate = formatter.parse(endDateStr);

            LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            end = end.plusDays(1);//将endDate也算进去

            for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
                WorkingDayPlan workingDayPlan = new WorkingDayPlan();
                workingDayPlan.setDate(date.toString());
                workingDayPlan.setWorkingDay(!isWeekend(date.toString()));
                list.add(workingDayPlan);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("日期转换错误");
        }
        return list;
    }

    public static int countWorkingDay(List<WorkingDayPlan> workingDayPlans){
        int count = 0;
        for (WorkingDayPlan plan : workingDayPlans){
            if(plan.isWorkingDay()){
                count ++;
            }
        }
        return count;
    }

}