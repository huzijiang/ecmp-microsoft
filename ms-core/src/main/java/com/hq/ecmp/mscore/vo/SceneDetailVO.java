package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/12 17:11
 */
@Data
public class SceneDetailVO {

    @ApiModelProperty(name = "name", value = "场景名称")
    private String name;

    @ApiModelProperty(name = "icon", value = "场景图标")
    private String icon;

    @ApiModelProperty(name = "regimenInfos", value = "用车制度信息集合")
    private List<Map<String,Long>> regimenInfos;

}
