package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/26 23:04
 */
@Data
public class CarGroupPhoneVO {

    @ApiModelProperty(name = "dispatchers",value = "车队调度员信息")
    private List<UserVO> dispatchers;

    @ApiModelProperty(name = "phone",value = "车队座机")
    private String phone;

    @ApiModelProperty(name = "phone",value = "车队名字")
    private String carGroupName;
}
