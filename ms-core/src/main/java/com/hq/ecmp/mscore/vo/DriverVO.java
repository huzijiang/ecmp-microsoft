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

}
