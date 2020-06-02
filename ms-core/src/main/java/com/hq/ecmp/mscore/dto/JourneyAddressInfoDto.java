package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 多个下车地点
 */
@Data
@ApiModel(value = "多个下车地点 --只对佛山包车")
public class JourneyAddressInfoDto {

    @ApiModelProperty(name = "journeyId", value = "行程id")
    private Long journeyId;

    @ApiModelProperty(name = "addressInfo", value = "下车地址")
    private String addressInfo;

    @ApiModelProperty(name = "createBy", value = "创建人")
    private String createBy;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "updateBy", value = "修改申请人")
    private String updateBy;

    @ApiModelProperty(name = "updateTime", value = "修改时间")
    private Date updateTime;
}
