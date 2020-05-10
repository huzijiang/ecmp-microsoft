package com.hq.ecmp.mscore.dto.config;

import lombok.Data;

import java.util.Date;

@Data
public class Scheduling {

    private Date calendarDate;
    private String calendarYear;
    private String calendarMonth;
    private String calendarDay;
    private String itIsWork;
    private String festivalName;
    private String workStart;
    private String workEnd;
    private String createBy;
    private Date createTime;
}
