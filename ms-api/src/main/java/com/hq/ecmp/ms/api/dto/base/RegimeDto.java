package com.hq.ecmp.ms.api.dto.base;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Author: zj.hu
 * @Date: 2020-01-02 18:26
 */
@Data
public class RegimeDto {
    /**
     * 用车制度编号
     */
    @NotEmpty
    @ApiParam(required = true)
    Long regimenId;


}
