package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>调度工作台数据</p>
 *
 * @author yj
 * @version 1.0
 * @date 2020/6/9 14:53
 */
@Data
@ApiModel("调度工作台订单相关model")
public class DisWorkBenchOrder {

    @ApiModelProperty(value = "订单号")
    private String orderNum;

    @ApiModelProperty(value = "用车单位")
    private String deptName;

    @ApiModelProperty(value = "用车人")
    private String useCarName;

    @ApiModelProperty(value = "车型")
    private String carTypeName;

    @ApiModelProperty(value = "目的地")
    private String endSite;

    @ApiModelProperty(value = "用车时间")
    private String useCarDate;

    @ApiModelProperty(value = "用车天数")
    private String useTime;
}
