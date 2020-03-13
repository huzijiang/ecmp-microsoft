package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/12 12:49
 */
@Data
public class SceneDTO {

    @ApiModelProperty(name = "sceneId", value = "场景id")
    private Long sceneId;

    @ApiModelProperty(name = "name", value = "场景名称")
    private String name;

    @ApiModelProperty(name = "icon", value = "场景图标")
    private String icon;

    @ApiModelProperty(name = "regimenIds", value = "用车制度id集合")
    private List<Long> regimenIds;
}
