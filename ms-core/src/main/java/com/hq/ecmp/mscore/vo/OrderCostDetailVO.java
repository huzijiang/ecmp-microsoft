package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xueyong
 * @date 2020/1/4
 * ecmp-proxy.
 */
@Data
@ApiModel(description = "订单费用模型")
public class OrderCostDetailVO {

    @ApiModelProperty(name = "customerPayPrice",value = "实际费用")
    private String customerPayPrice;
    @ApiModelProperty(name = "actualPayAmount",value = "优惠前金额")
    private String actualPayAmount;
    @ApiModelProperty(name = "couponSettleAmout",value = "优惠折扣金额")
    private String couponSettleAmout;
    @ApiModelProperty(name = "mileage",value = "总里程")
    private String mileage;
    @ApiModelProperty(name = "min",value = "总时长")
    private String min;
    private List<OtherCostVO> otherCost;

    public OrderCostDetailVO() {
    }

    public OrderCostDetailVO(String customerPayPrice, String mileage, String min) {
        this.customerPayPrice = customerPayPrice;
        this.mileage = mileage;
        this.min = min;
    }
}
