package com.hq.ecmp.ms.api.dto.order;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @auther: zj.hu
 * @date: 2020-07-20
 */
@Data
public class OrderUseTimeDto {

    /**
     * 订单编号
     */
    @NotNull
    @ApiParam(required = true)
    private Long orderId;

    /**
     * 订单编号
     */
    @ApiParam(required = false)
    private String orderNumber;

    /**
     * 订单编号
     */
    @NotNull
    @ApiParam(required = true)
    private String newUseTime;
}
