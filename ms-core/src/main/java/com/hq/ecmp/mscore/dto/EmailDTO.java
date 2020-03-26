package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
@ApiModel("邮箱信息")
public class EmailDTO {
    @ApiModelProperty(value = "邮箱ID")
    private Long Id;
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @ApiModelProperty(value = "邮箱地址")
    private String email;
    @ApiModelProperty(value = "状态")
    private String state;


}
