package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xueyong
 * @date 2020/1/4
 * ecmp-proxy.
 */
@Data
@ApiModel(description = "用户模型")
public class UserVO {

    private Long userId;

    @ApiModelProperty(name = "userName",value = "用户名")
    private String userName;

    @ApiModelProperty(name = "userPhone",value = "用户手机")
    private String userPhone;
}
