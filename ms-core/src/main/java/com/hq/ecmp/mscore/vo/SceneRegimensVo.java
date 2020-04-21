package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/4/21 17:17
 */
@Data
public class SceneRegimensVo {

    @ApiModelProperty(name = "icon",value = "场景图标")
    private String icon;

    @ApiModelProperty(name = "icon",value = "场景图标")
    private String sceneName;

    @ApiModelProperty(name = "icon",value = "场景图标")
    private Long sceneId;

    @ApiModelProperty(name = "regimenVOS",value = "场景对应的制度集合")
    private List<RegimenVO> regimenVOS;
}
