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
public class UserInvitationDTO {
    @ApiModelProperty(value = "邀请ID")
    private Long invitationId;
    @ApiParam(name = "name", value = "邀请名", required = true )
    private String name;
    @ApiParam(name = "enterpriseId", value = "所属公司", required = true )
    private Long enterpriseId;
    @ApiParam(name = "departmentId", value = "所属部门", required = true )
    private Long departmentId;
    @ApiParam(name = "roseId", value = "角色", required = true )
    private Long roseId;
    @ApiParam(name = "regimeIds", value = "用车制度", required = true )
    private String regimeIds;
    @ApiParam(name = "url", value = "url链接", required = true )
    private String url;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiParam(name = "state", value = "状态", required = true )
    private String state;
    @ApiParam(name = "type", value = "类型", required = true )
    private String type;
    @ApiParam(name = "apiUrl", value = "域名")
    private String apiUrl;

}
