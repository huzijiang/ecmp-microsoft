package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName CarLocationVo
 * @Description TODO
 * @Author yj
 * @Date 2020/3/18 12:41
 * @Version 1.0
 */
@ApiModel(value = "后管车辆检索出参model")
@Data
public class CarLocationVo {

    @ApiModelProperty(value = "精度")
    private String longitude;
    @ApiModelProperty(value = "纬度")
    private String latitude;
    @ApiModelProperty(value = "车型级别")
    private String level;
    @ApiModelProperty(value = "具体车型")
    private String carType;
    @ApiModelProperty(value = "车颜色")
    private String carColor;
    @ApiModelProperty(value = "服务状态（订单状态）")
    private String state;
    @ApiModelProperty(value = "订单编号")
    private String orderId;
    @ApiModelProperty(value = "司机姓名")
    private String driverName;
    @ApiModelProperty(value = "司机手机号")
    private String driverMobile;
}