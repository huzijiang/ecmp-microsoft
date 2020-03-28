package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/27 23:53
 */
@Data
public class DispatcherAndFixedLineDTO {

    @ApiModelProperty(name = "orderId",value = "订单id")
    private Long orderId;

    @ApiModelProperty(name = "traceId",value = "订单轨迹id")
    private Long traceId;
}
