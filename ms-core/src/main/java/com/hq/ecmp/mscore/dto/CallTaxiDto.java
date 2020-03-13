package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ClassName CallTaxiDto
 * @Description TODO
 * @Author yj
 * @Date 2020/3/11 11:23
 * @Version 1.0
 */

@Data
@ApiModel(value = "网约车接口model")
public class CallTaxiDto {
    /**
     * 订单编号
     */
    @NotNull
    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "下车经纬度",example = "100,200:逗号分隔")
    private String bookingEndPoint;
    @ApiModelProperty(value = "上车经纬度",example = "100,200:逗号分隔")
    private String  bookingStartPoint;
    @ApiModelProperty(value = "预定日期时间，10位时间戳(秒数)")
    private String bookingDate;
    @ApiModelProperty(value = "乘车人姓名")
    private String riderName;
    @ApiModelProperty(value = "乘车人电话")
    private String riderPhone;
    @ApiModelProperty(value = "预估金额")
    private String estimatedAmount;
    @ApiModelProperty(value = "下车地址")
    private String  bookingEndAddr;
    @ApiModelProperty(value = "上车地址")
    private String  bookingStartAddr;
    @ApiModelProperty(value = "下单车型，如“34”")
    private String groupId;
    @ApiModelProperty(value = "城市id(测试环境请用44)")
    private String cityId;

}
