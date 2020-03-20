package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
/**
 * @Author: shixin
 * @Date: 2020-03-18
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {

    /**
     *
     *注册用户类型：员工/驾驶员
     */
    @ApiParam(required = true)
    @NotEmpty
    @NotNull
    private String type;
    /**
     * 审核状态:申请中
     */
    @ApiParam(required = true)
    @NotEmpty
    @NotNull
    private String state;
}
