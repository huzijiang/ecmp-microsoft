package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "首页订单动态")
public class OrderSituationVO {


    @ApiModelProperty(value = "待派车数量")
    private int waitingCarCount;

    @ApiModelProperty(value = "已派车数量")
    private int goingCarCount;

    @ApiModelProperty(value = "已过期数量")
    private int expiredCarCount;

    @Override
    public String toString() {
        return "NewsVO{" +
                "userCount='" + waitingCarCount + '\'' +
                ", driverCount='" + goingCarCount + '\'' +
                ", carCount='" + expiredCarCount + '\'' +
                '}';
    }


}
