package com.hq.ecmp.mscore.vo;

import com.hq.ecmp.mscore.dto.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用车申请
 */
@ApiModel(value = "待确认订单model")
@Data
public class ToBeConfirmedOrderDto extends PageRequest {

    @ApiModelProperty(value = "申请id")
    private Long applyId;

    @ApiModelProperty(value = "申请人姓名")
    private String applyName;

    @ApiModelProperty(value = "申请人手机号")
    private String applyPhoneNumber;

    @ApiModelProperty(value = "用车人姓名")
    private String  vehicleUserName;

    @ApiModelProperty(value = "用车人手机号")
    private String vehicleUserPhoneNumber;

    @ApiModelProperty(value = "订单编号")
    private String orderNumber;

    @ApiModelProperty(value = "费用合计")
    private String amount;

    @ApiModelProperty(value = "同行人数")
    private String peerNumber;

    @ApiModelProperty(value = "包车类型")
    private String charterCarType;

    @ApiModelProperty(value = "用车方式")
    private String vehicleUseMode;

    @ApiModelProperty(value = "用车开始时间")
    private String beginTime;

    @ApiModelProperty(value = "用车结束时间")
    private String endTime;

    @ApiModelProperty(value = "申请原因")
    private String reason;

    @ApiModelProperty(value = "状态")
    private String orderState;

    @ApiModelProperty(value = "上车地点")
    private String beginAddress;

    @ApiModelProperty(value = "下车地点")
    private String endAddress;

}
