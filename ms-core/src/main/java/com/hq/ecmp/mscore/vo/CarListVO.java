package com.hq.ecmp.mscore.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/16 10:19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarListVO {

    //1.车辆品牌
    private String carType;

    //2.车牌号码
    private String carLicense;

    //3.能源类型
    private String fuelType;  //能源表

    //4.车型 行政级(代码)
    private String level;  //车型表

    //5.资产编号
    private String assetTag;

    //6.所属公司
    private Long ownerOrgId;

    //7.所属车队
    private String carGroupName;

    //8.可选驾驶员数量
    private List<DriverVO> driverInfo;
    private Integer driverNum;

    //9.车辆性质   车辆来源  //S001   购买   //s002   租赁  //S003   借调
    private String source;

    //10.车辆状态
    private String state;

    //11.车辆id
    private Long carId;

    //12.车型 行政级(名字)
    private String levelName;

    //13.车型 行政级(id)
    private Long carTypeId;
}
