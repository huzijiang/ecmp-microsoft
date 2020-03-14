package com.hq.ecmp.ms.api.dto.order;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: zj.hu
 * @Date: 2020-01-04 15:32
 */
@Data
public class OrderEvaluationDto {

    /**
     * 订单编号
     */
    @NotNull
    @ApiParam(required = true)
    private Long orderId;

    /**
     * 意见描述
     */
    private  String content;

    /** 建议*/
    private String result;

    /**图片路径*/
    private String[] imageUrls;
}