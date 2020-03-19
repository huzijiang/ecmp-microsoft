package com.hq.ecmp.mscore.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/11 11:21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
