package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName OrderHistoryTraceVo
 * @Description TODO
 * @Author yj
 * @Date 2020/3/19 16:13
 * @Version 1.0
 */

@Data
@ApiModel("订单历史轨迹入参")
public class OrderHistoryTraceDto {

    @ApiModelProperty(value = "订单id")
    private String orderId;
    @ApiModelProperty(value = "精度")
    private String longitude;
    @ApiModelProperty(value = "纬度")
    private String latitude;
    @ApiModelProperty(value = "时间")
    private Date createTime;
}
