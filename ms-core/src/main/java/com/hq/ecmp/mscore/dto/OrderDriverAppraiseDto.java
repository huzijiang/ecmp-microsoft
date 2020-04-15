package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName OrderDriverAppraiseRequest
 * @Description TODO
 * @Author yj
 * @Date 2020/3/6 15:27
 * @Version 1.0
 */
@ApiModel("订单世界评价入参")
@Data
public class OrderDriverAppraiseDto {

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "选项，Y001 道路熟悉" +
            "Y002 驾驶平顺\n" +
            "Y003 车内清新\n" +
            "Y004 非常礼貌\n" +
            "Y005 着装整齐\n" +
            "N001  道路不熟\n" +
            "N002  驾驶不稳\n" +
            "N003  车内异味\n" +
            "N004  开车打电话\n" +
            "N005  着装不整",notes = "多个按逗号分隔")
    private String item;

    @ApiModelProperty(value = "得分",notes = "0-10分  对应 5颗星,比如：3分 代表1颗半星")
    private String  score;

    @ApiModelProperty(value = "评价内容")
    private String content;

    @ApiModelProperty(value = "司机id")
    private Long driverId;

    @ApiModelProperty(value = "汽车id")
    private Long cardId;
}
