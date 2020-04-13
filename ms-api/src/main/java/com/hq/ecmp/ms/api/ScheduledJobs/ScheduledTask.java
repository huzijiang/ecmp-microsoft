package com.hq.ecmp.ms.api.ScheduledJobs;

import com.hq.ecmp.mscore.service.IApplyInfoService;
import com.hq.ecmp.mscore.service.IEcmpNoticeService;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import com.hq.ecmp.mscore.service.IProjectInfoService;
import com.hq.ecmp.mscore.service.impl.OrderInfoServiceImpl;
import com.hq.ecmp.util.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);

    @Autowired
    private IProjectInfoService iProjectInfoService;

    @Autowired
    private IEcmpUserService ecmpUserService;
    @Autowired
    private IApplyInfoService applyInfoService;

    private OrderInfoServiceImpl orderInfoService;

    @Autowired
    private IEcmpNoticeService iEcmpNoticeService;

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

    //每天0点0分校验员工是否已离职
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkDimissionEcmpUser(){
        System.out.println("定时任务:checkDimissionEcmpUser:校验员工是否离职"+ DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
        ecmpUserService.checkDimissionEcmpUser();
    }
    //每天0点0分校验申请单是否过期
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkApplyExpired(){
        System.out.println("定时任务:checkApplyExpired:校验申请单是否过期"+ DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
        applyInfoService.checkApplyExpired();
    }

    //每天凌晨一点判断订单是否过期
    @Scheduled(cron = "0 0 1 * * ? ")
    public void checkOrderIsExpired(){
        System.out.println("定时任务:checkOrderIsExpired:校验订单是否过期"+ DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
        orderInfoService.checkOrderIsExpired();
    }

    //后台公告管理通过发布时间与结束时间做状态修改
    //@Scheduled(cron = "0 0 1 * * ? ")
    public void  announcementManagementTimingTask (){
        log.info("定时任务:announcementManagementTimingTask:通过发布时间与结束时间做状态修改StartTime:"+ DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
        //System.out.println("定时任务:announcementManagementTimingTask:通过发布时间与结束时间做状态修改Start:"+ DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
        try {
            iEcmpNoticeService.announcementTask();
        }catch (Exception e) {
            e.printStackTrace();
        }
        log.info("定时任务:announcementManagementTimingTask:通过发布时间与结束时间做状态修改EndTime:"+ DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
        //System.out.println("定时任务:announcementManagementTimingTask:通过发布时间与结束时间做状态修改End:"+ DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT,new Date()));
    }

}