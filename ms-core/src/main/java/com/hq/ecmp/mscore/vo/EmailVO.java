package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import sun.awt.image.IntegerInterleavedRaster;

@Data
@ApiModel(description = "邮箱信息")
public class EmailVO {

    /**
     * 邮箱ID
     */
    @ApiModelProperty(value = "邮箱ID")
    private Long Id;
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;





}
