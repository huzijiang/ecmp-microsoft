package com.hq.ecmp.mscore.dto.statistics;

import lombok.Data;

import java.util.List;

@Data
public class StatisticsParam {
    //部门集合
    private List<Long> deptIds;
    //车队集合
    private List<Long> carGroupIds;
    //开始时间
    private String beginDate;
    //结束时间
    private String endDate;
    //订单-数据类型
    //运力-统计维度 0公司 1车队
    private int type;
    //公司id
    private Long companyId;
}
