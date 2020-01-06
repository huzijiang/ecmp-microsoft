package com.hq.ecmp.ms.api.dto.journey;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author: zj.hu
 * @Date: 2020-01-06 14:46
 */
@Data
public class JourneyNodeDto {
    /**
     * 申请单编号
     */
    @NotEmpty
    @NotNull
    private Long applyId;
    /**
     * 行程单编号
     */
    @NotEmpty
    @NotNull
    private Long jouneyId;

    /**
     * 行程节点编号
     */
    @ApiParam(required = true)
    @NotEmpty
    @NotNull
    private Long jouneyNodeId;


}
