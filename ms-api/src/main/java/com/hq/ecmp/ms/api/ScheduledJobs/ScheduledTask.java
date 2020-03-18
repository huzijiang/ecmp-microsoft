package com.hq.ecmp.ms.api.ScheduledJobs;

import com.hq.ecmp.mscore.service.IProjectInfoService;
import com.hq.ecmp.util.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {

    @Autowired
    private IProjectInfoService iProjectInfoService;

    @Scheduled(cron = "5 * * * * ?")
    public void testJob(){
        System.out.println("定时任务:testJob"+ DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
    }

    //每天0点5分校验项目是否失效
    @Scheduled(cron = "0 5 0 * * ?")
    public void checkProject(){
        System.out.println("定时任务:checkProject:校验项目是否过期"+ DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
        iProjectInfoService.checkProject();
    }

}