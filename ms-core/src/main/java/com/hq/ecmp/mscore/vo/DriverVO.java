package com.hq.ecmp.mscore.vo;

import lombok.Data;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/15 10:37
 */
@Data
public class DriverVO {

    private Long driverId;  //驾驶员id

    private Long userId;   //司机对应的用户id 也就是员工id

    private String driverName; //驾驶员名字

    private String driverMobile;//驾驶员手机号

    private String itIsFullTime; //编制是否专职  Z000  合同制， Z001  在编, Z002  外聘 ,Z003  借调

    private String carGroupName;//所属车队名字

    private Long carGroupId;//所属车队Id

    private int carCount; //可用车辆数

    private String workState; //工作状态  X000  病假,X002  年假,X003  公休,X999

    private String  company; //所属公司






}
