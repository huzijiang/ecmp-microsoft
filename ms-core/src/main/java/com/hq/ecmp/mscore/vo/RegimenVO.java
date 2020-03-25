package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: chao.zhang
 * @Date: 2020/2/27 10:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegimenVO {

    /**
     * 用车制度ID
     */
    private Integer regimenId;

    /**
     * 用车制度模板ID
     */
    private Integer templateId;  //TODO 新增

    /**
     * 用车制度名称
     */
    private String name;

    /**
     * 用车制度图标
     */
    @ApiModelProperty(name = "icon",value = "用车制度图标")
    private String icon;    //TODO 数据库表中暂无此字段

    /**
     * 用车方式 eg 自有车、网约车
     */
    @ApiModelProperty(name = "carType",value = "用车方式 eg 自有车、网约车")
    private String canUseCarMode;

    @ApiModelProperty(name = "regimenType",value = "A001-公务用车  A002-差旅用车")
    private String regimenType; //TODO 新增
}
