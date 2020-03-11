package com.hq.ecmp.mscore.vo;

import lombok.Data;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/11 11:21
 */
@Data
public class DriverDutyPlanVO {

    /**
     * 司机排班日期
     */
    private String dutyDate;

    /**
     * 排班的状态 eg 1正常 2请假
     */
    private String status;
}
