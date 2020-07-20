package com.hq.ecmp.ms.api.dto.journey;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @auther: zj.hu
 * @date: 2020-07-20
 */
@Data
public class JourneyApplyOrderDto {
    /**
     * 申请单编号
     */
    @ApiParam(required = false)
    @NotEmpty
    @NotNull
    private Long applyId;
    /**
     * 行程单编号
     */
    @ApiParam(required = false)
    @NotEmpty
    @NotNull
    private Long jouneyId;

    /**
     * 订单编号
     */
    @ApiParam(required = true)
    @NotEmpty
    @NotNull
    private String orderNumber;
}
