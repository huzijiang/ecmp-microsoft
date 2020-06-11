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
public class StatisticsForAdminVo {

    //用车部门
    @ApiModelProperty(value = "用车部门")
    private String deptName;

    //总次数
    @ApiModelProperty(value = "总次数")
    private Long orders;

    //总费用
    @ApiModelProperty(value = "总费用")
    private BigDecimal amount;

    //累计用车天数
    @ApiModelProperty(value = "累计用车天数")
    private Long useTimes;

    //内部总次数
    @ApiModelProperty(value = "内部总次数")
    private Long ordersByIn;

    //内部总费用
    @ApiModelProperty(value = "内部总费用")
    private BigDecimal amountByIn;

    //内部累计用车天数
    @ApiModelProperty(value = "内部累计用车天数")
    private Long useTimesByIn;

    //外部车队总次数
    @ApiModelProperty(value = "外部车队总次数")
    private Long ordersByOut;

    //外部车队总费用
    @ApiModelProperty(value = "外部车队总费用")
    private BigDecimal amountByOut;

    //外部车队累计用车天数
    @ApiModelProperty(value = "外部车队累计用车天数")
    private Long useTimesByOut;

    //车牌号
    @ApiModelProperty(value = "车牌号")
    private String carLicense;

    //驾驶员姓名
    @ApiModelProperty(value = "驾驶员姓名")
    private String driverName;

    //车型名字
    @ApiModelProperty(value = "车型名字")
    private String carName;

    //座位数
    @ApiModelProperty(value = "座位数")
    private String seatNum;

    //外部车队名称
    @ApiModelProperty(value = "外部车队名称")
    private String carGroupName;
}
