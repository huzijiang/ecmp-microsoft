package com.hq.ecmp.ms.api.dto.car;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Author: zj.hu
 * @Date: 2020-01-04 17:28
 */
@Data
public class CarDto {

    @NotEmpty
    @ApiParam(required = true)
    private Long carId;
}
