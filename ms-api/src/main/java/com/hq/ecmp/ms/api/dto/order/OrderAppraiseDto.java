package com.hq.ecmp.ms.api.dto.order;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 *
 * 订单评价
 * @Author: zj.hu
 * @Date: 2020-01-04 16:57
 */
@Data
public class OrderAppraiseDto {

    /**
     * 订单编号
     */
    @NotEmpty
    @ApiParam(required = true)
    private Long  orderId;


    /**
     * 评价内容
     */
    @NotEmpty
    @ApiParam(required = true)
    private String appraise;

}
