package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author:
 * @Date: 2020-03-17 15:26
 */
@Data
public class InvitationDto {



    /**
     * 邀请ID
     */
    @ApiParam(required = true)
    @NotEmpty
    @NotNull
    private Long invitationId;
    /**
     * 邀请状态:停用/启用
     */
    @ApiParam(required = true)
    @NotEmpty
    @NotNull
    private String state;

    @ApiParam(required = true)
    @NotEmpty
    @NotNull
    private Date updateTime;

}
