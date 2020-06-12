package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ghb
 * @description 统计返回vo类
 * @date 2020/6/9
 */

@Data
public class StatisticsForAdminDetailVo {

    //订单号
    @ApiModelProperty(value = "订单号")
    private String orderId;

    //用车部门
    @ApiModelProperty(value = "用车部门")
    private String deptName;

    //用车时间
    @ApiModelProperty(value = "用车时间")
    private String beginTime;

    //用车人
    @ApiModelProperty(value = "用车人")
    private String vehicleUserName;

    //车型
    @ApiModelProperty(value = "车型")
    private String carName;

    //车牌号
    @ApiModelProperty(value = "车牌号")
    private String carLicense;

    //出发地
    @ApiModelProperty(value = "出发地")
    private String beginAddress;

    //目的地
    @ApiModelProperty(value = "目的地")
    private String endAddress;

    //用车天数
    @ApiModelProperty(value = "用车天数")
    private String useTime;
}
