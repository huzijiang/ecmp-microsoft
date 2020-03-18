package com.hq.ecmp.ms.api.dto.invitation;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author: shixin
 * @Date: 2020-03-16 15:26
 */
@Data
public class InvitationDto {



    /**
     * 邀请编号
     */
    @ApiParam(required = true)
    @NotEmpty
    @NotNull
    private Long invitationId;
    /**
     * 邀请状态
     */
    @ApiParam(required = true)
    @NotEmpty
    @NotNull
    private String state;


}
