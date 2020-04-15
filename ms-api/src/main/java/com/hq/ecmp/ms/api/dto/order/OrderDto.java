package com.hq.ecmp.ms.api.dto.order;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;

/**
 * @Author: zj.hu
 * @Date: 2020-01-04 15:32
 */
@Data
public class OrderDto {

    /**
     * 订单编号
     */
    @NotNull
    @ApiParam(required = true)
    private Long orderId;

    /**
     * 取消原因
     */
//    @NotNull
//    @ApiParam(required = true)
    private  String cancelReason;
    @NotNull
    @ApiParam(required = true)
    private String flag;
}
