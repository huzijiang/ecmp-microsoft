package com.hq.ecmp.mscore.dto;


import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.Date;

/**
 * @author
 * @date 2020-03-17
 */
@Data
public class UserInvitationUrlDTO {
    @ApiModelProperty(value = "邀请ID")
    private Long invitationId;
    @ApiParam(name = "url", value = "url链接", required = true )
    private String url;


}
