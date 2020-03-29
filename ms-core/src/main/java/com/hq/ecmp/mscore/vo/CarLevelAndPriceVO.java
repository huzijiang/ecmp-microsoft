package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/20 19:02
 */
@Data
public class CarLevelAndPriceVO {

    @ApiModelProperty(name = "onlineCarLevel",value = "网约车型",example = "P001")
    private String onlineCarLevel;
    @ApiModelProperty(name = "estimatePrice",value = "预估价格,原价")
    private Integer estimatePrice;

    @ApiModelProperty(name = "amount",value = "车费预估-优惠后金额")
    private Integer amount;

   // @ApiModelProperty(name = "dynamic_price",value = "折扣价格")
   // private String dynamic_price;

    @ApiModelProperty(name = "duration",value = "预估用时")
    private Integer duration;

    @ApiModelProperty(name = "source",value = "平台来源")
    private String source;

    @ApiModelProperty(name = "groupId",value = "云端车型id")
    private Integer groupId;

}
