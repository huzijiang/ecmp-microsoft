package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/13 10:26
 */
@Data
public class SceneSortDTO {

    @ApiModelProperty(name = "mainSceneId", value = "主场景id")
    private Long mainSceneId;

    @ApiModelProperty(name = "targetSceneId", value = "目标场景id")
    private Long targetSceneId;

}
