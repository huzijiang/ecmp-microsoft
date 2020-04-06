package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RegisterVO {

    @ApiModelProperty(value = "人数")
    private int registerCount;

    @ApiModelProperty(value = "人数")
    private int resignationCount;



}
