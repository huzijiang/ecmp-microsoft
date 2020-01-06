package com.hq.ecmp.ms.api.dto.threeparty;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Author: zj.hu
 * @Date: 2020-01-04 14:22
 */
@Data
public class FlightDto {

    /**
     *航班号
     */
    /**
     * 用车制度编号
     */
    @NotEmpty
    @ApiParam(required = true)
    private String fltNo;
}
