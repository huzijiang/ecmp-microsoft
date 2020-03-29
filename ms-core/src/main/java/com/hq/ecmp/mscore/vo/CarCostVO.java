package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xueyong
 * @date 2020/1/4
 * ecmp-proxy.
 */
@Data
@ApiModel(description = "地址模型")
public class CarCostVO {

    @ApiModelProperty(name = "amount",value = "车费预估-优惠后金额")
    private Integer amount;
    @ApiModelProperty(name = "baseFee",value = "套餐费")
    private Integer baseFee;
    @ApiModelProperty(name = "disMoney",value = "原价")
    private Integer disMoney;
    @ApiModelProperty(name = "distance",value = "里程")
    private Integer distance;
    @ApiModelProperty(name = "distanceFee",value = "里程费")
    private Integer distanceFee;
    @ApiModelProperty(name = "duration",value = "时长（分钟）")
    private String duration;
    @ApiModelProperty(name = "durationFee",value = "时长费")
    private Integer durationFee;
    @ApiModelProperty(name = "dynamic_price",value = "折扣价格")
    private String dynamic_price;
    @ApiModelProperty(name = "groupId",value = "车型id")
    private String groupId;
    @ApiModelProperty(name = " groupName",value = "车型名称")
    private String groupName;
    @ApiModelProperty(name = "overDistancePrice",value = "每公里单价")
    private Double overDistancePrice;
    @ApiModelProperty(name = "tolls",value = "高速费")
    private Double tolls;
    @ApiModelProperty(value = "来源平台")
    private String source;

}
