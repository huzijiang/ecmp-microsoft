package com.hq.ecmp.mscore.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "修改申请单所需要的id")
public class ApplySingleIdVO {

    /**
     * 申请id
     */
    @ApiModelProperty(name = "applyId", value = "申请id", required = true, position = 4)
    private Long applyId;

    /**
     * 行程journeyId
     */
    @ApiModelProperty(name = "journeyId", value = "行程id", required = true, position = 4)
    private Long journeyId;

    /**
     * journeyId
     */
    @ApiModelProperty(name = "nodeId", value = "节点id", required = true, position = 4)
    private Long nodeId;

    /**
     * journeyPassengerId
     */
    @ApiModelProperty(name = "journeyPassengerId", value = "乘车id", required = true, position = 4)
    private Long journeyPassengerId;

    /**
     * priceId
     */
    @ApiModelProperty(name = "priceId", value = "价格id", required = true, position = 4)
    private Long priceId;

    /**
     * orderId
     */
    @ApiModelProperty(name = "orderId", value = "订单id", required = true, position = 4)
    private Long orderId;

    @ApiModelProperty(name = "powerId", value = "powerId", required = true, position = 4)
    private Long powerId;

    @ApiModelProperty(name = "dispatchId", value = "dispatchId", required = true, position = 4)
    private Long dispatchId;
}
