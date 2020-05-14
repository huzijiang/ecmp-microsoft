package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("补单model--为了计算而用")
public class SupplementVO {

    @ApiModelProperty(value = "车型id")
    private Long carTypeId;

    @ApiModelProperty(value = "城市code")
    private String cityCode;

    @ApiModelProperty(value = "等待时间")
    private BigDecimal waitingTime;

    @ApiModelProperty(name = "totalTime", value = "订单总时长")
    private Integer totalTime;

    @ApiModelProperty(name = "totalMileage", value = "订单总里程")
    private BigDecimal totalMileage;

}
