package com.hq.ecmp.mscore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hq.core.web.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Data;


import java.util.Date;

@Data
@ApiModel("邮箱信息")
public class EmailDTO {
   // @ApiModelProperty(value = "邮箱ID")
  //  private Long Id;
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @ApiModelProperty(value = "邮箱地址")
    private String email;
    @ApiModelProperty(value = "状态")
    private String state;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;




}
