package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/29 9:29
 */
@Data
public class CarGroupFixedPhoneVO {

    @ApiModelProperty(name = "phone",value = "车队座机")
    private String phone;

    @ApiModelProperty(name = "phone",value = "车队名字")
    private String carGroupName;
}
