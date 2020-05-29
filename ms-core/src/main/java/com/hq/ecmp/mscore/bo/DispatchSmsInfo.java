package com.hq.ecmp.mscore.bo;

import lombok.Data;

/**
 * @ClassName
 * @Description TODO
 * @Author yj
 * @Date 2020/5/27 15:50
 * @Version 1.0
 */
@Data
public class DispatchSmsInfo {

    /**
     * 内部调度员车队名字
     */
    private String innerCarGroupName;
    /**
     * 内部调度员姓名
     */
    private String innerDispatcherName;

    /**
     * 内部调度员电话
     */
    private String innerDispatcherMobile;

    /**
     * 用车人姓名
     */
    private String useCarName;

    /**
     * 用车人电话
     */
    private String useCarMobile;

    /**
     * 预约用车天数
     */
    private String useTime;

    /**
     * 预计用车时间（yyyy年MM日dd天 HH:mm）
     */
    private String useCarTime;

    /**
     * 用车备注
     */
    private String notes;

}
