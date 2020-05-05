package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xueyong
 * @date 2020/1/4
 * ecmp-proxy.
 */
@Data
@ApiModel(description = "看板模型")
public class DriverOrderVo {

    @ApiModelProperty(name = "driverId",value = "司机id")
    private Long driverId;
    @ApiModelProperty(name = "carId",value = "车辆id")
    private Long carId;
    @ApiModelProperty(name = "driverName",value = "司机姓名")
    private String driverName;
    @ApiModelProperty(name = "driverPhone",value = "司机电话")
    private String driverPhone;
    @ApiModelProperty(name = "date",value = "")
    private String date;
    @ApiModelProperty(name = "carLicense",value = "车牌号")
    private String carLicense;
    @ApiModelProperty(name = "carName",value = "车辆名称")
    private String carName;
    @ApiModelProperty(name = "carType",value = "che类型")
    private String carType;
    private List<OrderDetailVO> orderDetailVOS;


}
