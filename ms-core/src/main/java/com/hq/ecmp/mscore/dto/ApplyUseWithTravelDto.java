package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName ApplyUseWithTravelDto
 * @Description TODO
 * @Author yj
 * @Date 2020/3/7 13:37
 * @Version 1.0
 */
@Data
public class ApplyUseWithTravelDto extends ParallelOrderDto {

    @ApiModelProperty(name = "type", value = "类型",notes = "1:随叫随到;2:预约用车;3:接机;4:包车; 5:送机 ")
    private String type;
//    @ApiModelProperty(value = "乘车人姓名")
//    private String riderName;
//    @ApiModelProperty(value = "乘车人电话")
//    private String riderPhone;
//    @ApiModelProperty(value = "预估金额")
//    private String estimatedAmount;
    @ApiModelProperty(value = "下单车型，逗号分隔",notes = "P001-公务级\n" +
            "P002-行政级\n" +
            "P003-六座商务")
    private String groupId;
    @ApiModelProperty(value = "城市id(测试环境请用44)")
    private String cityId;
    @ApiModelProperty(value = "订单状态",hidden = true)
    private String orderStatus;
    @ApiModelProperty(value = "出发地三字码",notes = "接机时需传")
    private String  depCode;
    @ApiModelProperty(value = "目的地三字码",notes = "接机时需传")
    private String  arrCode;
    @ApiModelProperty(value = "航班号",notes = "接机时需传")
    private String   airlineNum;
    @ApiModelProperty(value = "航班计划出发日期,格式(yyyy-MM-dd HH:mm:ss)",notes = "接机时需传")
    private String   planDate;
    @ApiModelProperty(value = "1.走调度（包含自有车）  2 直接约车（只有网约车）")
    private int isDispatch;
    @ApiModelProperty(value = "预估时长(分钟),isDispatch 为2 时传")
    private String duration;
    @ApiModelProperty(value = "预估价格参考开始时间，isDispatch 为2 时传")
    private String calculatePriceStartTime;
    @ApiModelProperty(value = "预估价格来源，isDispatch 为2 时传")
    private String source;
}
