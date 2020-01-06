package com.hq.ecmp.ms.api.dto.car;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Author: zj.hu
 * @Date: 2020-01-06 15:14
 */
@Data
public class EmergencyContactDto {

    /**
     * 紧急联系人 电话
     */
    @NotEmpty
    @ApiParam(required = true)
    private  String emergencyContactPhone;
}
