package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/12 18:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SceneListVO {

    @ApiModelProperty(name = "sceneId", value = "场景id")
    private Long sceneId;

    @ApiModelProperty(name = "name", value = "场景名称")
    private String name;

    @ApiModelProperty(name = "icon", value = "场景图标")
    private String icon;

    @ApiModelProperty(name = "sortNo", value = "排序序列号")
    private Long sortNo;

    @ApiModelProperty(name = "regimenInfos", value = "用车制度信息集合")
    private List<String> regimenNames;

    /**
     * 生效状态
     * 0 立即生效
     * 1 不生效
     */
    @ApiModelProperty(name = "effectStatus", value = "生效状态 0 立即生效 1 不生效")
    private String effectStatus;
}
