package com.hq.ecmp.ms.api.dto.invoice;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author: zj.hu
 * @Date: 2020-01-03 17:41
 */
@Data
public class InvoiceDto {

    /**
     * 发票查询开始时间
     */
    @ApiParam(required = true)
    @NotEmpty
    @NotNull
    private String startTime;
    /**
     * 发票查询结束时间
     */
    @ApiParam(required = true)
    @NotEmpty
    @NotNull
    private String endTime;
    /**
     * 开票状态
     */
    @ApiParam(required = true)
    @NotEmpty
    @NotNull
    private String state;


}
