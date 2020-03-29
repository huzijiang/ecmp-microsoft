package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @ClassName DriverCloudDto
 * @Description TODO
 * @Author yj
 * @Date 2020/3/25 14:59
 * @Version 1.0
 */
@Data
@ApiModel("网约车司机信息")
public class DriverCloudDto {
    //云端司机id
    private  Long driverId;
    //司机评分
    private String driverRate;
    //车型 P001,P002
    private String groupName;
    //车牌
    private String licensePlates;
    //车辆类型
    private String modelName;
    //司机名字
    private String driverName;
    //司机电话
    private String  phone;
    //
    private String vehiclePic;
    //车颜色
    private String vehicleColor;
    //坐标
    private String driverPoint;

}
