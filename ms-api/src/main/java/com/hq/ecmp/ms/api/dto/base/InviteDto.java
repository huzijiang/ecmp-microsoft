package com.hq.ecmp.ms.api.dto.base;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Author: CAOBJ
 * @Date: 2020-01-02 18:26
 */
@Data
public class InviteDto {
    /**
     * 用车制度编号
     */
    @ApiParam(required = false)
    Long deptId;
    //T001员工,T002驾驶员
    @ApiParam(required = true)
    String type;
    @ApiParam(required = false)
    String state;


}
