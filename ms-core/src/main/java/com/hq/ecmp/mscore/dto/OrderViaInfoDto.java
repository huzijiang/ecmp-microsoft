package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName OrderViaInfoDto
 * @Description TODO
 * @Author yj
 * @Date 2020/3/12 18:54
 * @Version 1.0
 */
@Data
@ApiModel("订单途径地列表model")
public class OrderViaInfoDto {


    @ApiModelProperty(value = "途径地id")
    private Long viaId;

    @ApiModelProperty(value = "订单号")
    private Long orderId;

    @ApiModelProperty(value = "精度")
    private Double longitude;

    @ApiModelProperty(value = "纬度")
    private Double latitude;

    @ApiModelProperty(value = "短地址")
    private String shortAddress;

    @ApiModelProperty(value = "长地址")
    private String fullAddress;

    @ApiModelProperty(value = "序号")
    private Integer sortNumber;
}
