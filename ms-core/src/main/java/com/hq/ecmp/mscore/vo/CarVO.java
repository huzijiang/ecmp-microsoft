package com.hq.ecmp.mscore.vo;

import lombok.Data;

/**
 * @Author: xin.shi
 * @Date: 2020/3/15 10:37
 */
@Data
public class CarVO {

    private Long carId;  //车辆id

    private Long userId;   //司机对应的用户id 也就是员工id

    private Long driverId;


}
