package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户邀请信息
 * @author shixin
 * @date 2020/3/17
 */
@Data
@ApiModel(description = "用户邀请列表")
public class InvitationUrlVO {

    /**
     * 链接
     */
    @ApiModelProperty(name = "url", value = "URL链接")
    private String url;

}
