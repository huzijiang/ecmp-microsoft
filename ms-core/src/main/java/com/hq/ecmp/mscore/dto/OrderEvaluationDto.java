package com.hq.ecmp.mscore.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private String type;

    /**
     * 意见描述
     */
    private  String content;

    /**图片路径*/
    private String imgUrls;
}
