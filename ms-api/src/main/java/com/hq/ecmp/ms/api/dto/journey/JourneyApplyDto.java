package com.hq.ecmp.ms.api.dto.journey;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author: zj.hu
 * @Date: 2020-01-03 17:41
 */
@Data
public class JourneyApplyDto {

    /**
     * 申请单编号
     */
    @ApiParam(required = true)
    @NotEmpty
    @NotNull
    private Long applyId;
    /**
     * 行程单编号
     */
    @ApiParam(required = true)
    @NotEmpty
    @NotNull
    private Long jouneyId;



}
