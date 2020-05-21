package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class OrderReassignVO {

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "状态")
    private Long state;
    @ApiModelProperty(value = "订单id")
    private Long lableState;
    @ApiModelProperty(value = "申请改派原因")
    private String applyReason;

    @ApiModelProperty(value = "驳回原因")
    private String rejectReason;
    private List<RejectDispatcherUserVO> approveList;


}
