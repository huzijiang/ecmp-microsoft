package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/20 14:53
 */
@Data
public class RegimenDTO {

    @ApiModelProperty(name = "regimenId",value = "用车制度id",required = true)
    private Long regimenId;

    @ApiModelProperty(name = "type",value = "用车权限类型 \n" +
            "差旅：\n" +
            "C001  接机\n" +
            "C009  送机\n" +
            "C222  市内用车",required = false)
    private String type;
}
