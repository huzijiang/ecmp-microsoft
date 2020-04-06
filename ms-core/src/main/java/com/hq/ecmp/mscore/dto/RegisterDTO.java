package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author:
 * @Date: 2020-03-18
 */

@Data
public class RegisterDTO {

    /**
     * 注册id
     */
    @ApiParam(required = true)
    @NotEmpty
    @NotNull
    private Long registerId;
    /**
     * 注册状态：拒绝/通过
     */

    @ApiParam(required = true)
    @NotEmpty
    @NotNull
    private String state;

    @ApiParam(required = true)
    @NotEmpty
    @NotNull
    private Date updateTime;

    @ApiParam(value = "注册用户类型：员工/驾驶员")
    private String type;

}
