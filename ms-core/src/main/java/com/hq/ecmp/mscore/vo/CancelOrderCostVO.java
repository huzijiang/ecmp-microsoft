package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xueyong
 * @date 2020/1/4
 * ecmp-proxy.
 */
@Data
@ApiModel(description = "取消订单模型")
public class CancelOrderCostVO {

    @ApiModelProperty(name = "amount",value = "取消订单总费用=ownerAmount+personalAmount")
    private String cancelAmount;
    @ApiModelProperty(name = "ownerAmount",value = "企业支付的取消费")
    private String ownerAmount;
    @ApiModelProperty(name = "personalAmount",value = "个人支付的取消费")
    private String personalAmount;
    @ApiModelProperty(name = "payId",value = "payId")
    private String payId;
    @ApiModelProperty(name = "payState",value = "payState")
    private String payState;
    @ApiModelProperty(name = " carType",value = "车型名称")
    private String carType;
    @ApiModelProperty(name = " isPayFee",value = "是否支付取消费:1有取消费,0无")
    private int isPayFee;
}
