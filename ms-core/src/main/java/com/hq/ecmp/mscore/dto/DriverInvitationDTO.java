package com.hq.ecmp.mscore.dto;


import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.Date;

/**
 * @author  shixin
 * @date 2020-03-17
 */
@Data
public class DriverInvitationDTO {
    @ApiParam(name = "name", value = "邀请名", required = true )
    private String name;
    @ApiParam(name = "enterpriseId", value = "所属公司", required = true )
    private Long enterpriseId;
    @ApiParam(name = "carGroupId", value = "所属车队", required = true )
    private Long carGroupId;
    @ApiParam(name = "roseId", value = "角色", required = true )
    private Long roseId;
    @ApiParam(name = "regimeIds", value = "可用车辆", required = true )
    private String regimeIds;
    @ApiParam(name = "url", value = "url链接", required = true )
    private String url;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiParam(name = "state", value = "状态", required = true )
    private String state;
    @ApiParam(name = "type", value = "类型", required = true )
    private String type;

}
