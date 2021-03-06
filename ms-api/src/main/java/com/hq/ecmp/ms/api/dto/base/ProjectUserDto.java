package com.hq.ecmp.ms.api.dto.base;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Author: zj.hu
 * @Date: 2020-01-04 17:28
 */
@Data
public class ProjectUserDto {

    @NotEmpty
    @ApiParam(required = true)
    private Long projectId;
    @NotEmpty
    @ApiParam(required = true)
    private Long userId;  //驾驶员员工编号

}
