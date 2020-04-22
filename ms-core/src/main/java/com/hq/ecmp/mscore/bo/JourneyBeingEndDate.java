package com.hq.ecmp.mscore.bo;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName
 * @Description 行程的有效开始时间和结束时间（计划时间的基础上结合制度的前后天数计算得出）
 * @Author yj
 * @Date 2020/4/21 21:03
 * @Version 1.0
 */
@Data
public class JourneyBeingEndDate {
    private Date beginDate;
    private Date endDate;
}
