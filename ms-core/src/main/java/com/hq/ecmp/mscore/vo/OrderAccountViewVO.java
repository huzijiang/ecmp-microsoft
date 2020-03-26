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
@ApiModel(description = "账单总览")
public class OrderAccountViewVO {

    @ApiModelProperty(name = "accountDate",value = "账期")
    private String accountDate;
    @ApiModelProperty(name = "orderNum",value = "订单数")
    private String orderNum;
    @ApiModelProperty(name = "amount",value = "订单总费用")
    private String amount;
    @ApiModelProperty(name = "amountOut",value = "服务费")
    private String amountOut;
    @ApiModelProperty(name = "desc",value = "账期描述")
    private String desc;
    @Override
    public String toString() {
        return "OrderAccountViewVO{" +
                "accountDate='" + accountDate + '\'' +
                ",desc='" + desc + '\'' +
                ", orderNum='" + orderNum + '\'' +
                ", amount='" + amount + '\'' +
                ", amount='" + amountOut + '\'' +
                '}';
    }



}
