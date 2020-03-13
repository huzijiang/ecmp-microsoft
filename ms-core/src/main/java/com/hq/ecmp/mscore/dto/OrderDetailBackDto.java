package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName OrderDetailBackDto
 * @Description TODO
 * @Author yj
 * @Date 2020/3/13 18:12
 * @Version 1.0
 */
@Data
@ApiModel(value = "订单详情model")
public class OrderDetailBackDto extends OrderListBackDto {

    @ApiModelProperty(value = "用车制度")
    private String useCarRegime;
    @ApiModelProperty(value = "司机名字")
    private String driverName;
    @ApiModelProperty(value = "司机电话")
    private String driverPhone;
    @ApiModelProperty(value = "车牌")
    private String carLicense;
    @ApiModelProperty(value = "车型")
    private String carType;
    @ApiModelProperty(value = "车的颜色")
    private String carColor;

    @ApiModelProperty(value = "计划用车时间")
    private String useCarTime;
    @ApiModelProperty(value ="计划上车地点")
    private String planningSetOutAddress;
    @ApiModelProperty(value ="计划下车时间")
    private String planningArriveTime;
    @ApiModelProperty(value ="计划下车地点")
    private String planningArriveAddress;

    @ApiModelProperty(value ="车辆级别")
    private String carLevel;
    @ApiModelProperty(value ="行程时长")
    private String totalTime;
    @ApiModelProperty(value ="行程里程")
    private String totalMileage;

    @ApiModelProperty(value ="费用详情",notes = "网约车需要")
    private String amountDetail;



}
