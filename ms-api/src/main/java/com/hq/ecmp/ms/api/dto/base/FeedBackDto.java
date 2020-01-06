package com.hq.ecmp.ms.api.dto.base;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 *
 * 意见反馈
 * @Author: zj.hu
 * @Date: 2020-01-06 15:40
 */
@Data
public class FeedBackDto {

    /**
     * 司机编号
     */
    @ApiParam(required = true)
    @NotEmpty
    private Long userId;


    /**
     * 反馈内容
     */
    private String Content;



}
