package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName OrderListBackDto
 * @Description TODO
 * @Author yj
 * @Date 2020/3/13 16:26
 * @Version 1.0
 */
@Data
@ApiModel(value = "pc端-订单列表出参model和入参model")
public class OrderListBackDto extends PageRequest {

    @ApiModelProperty(value = "申请人姓名")
    private String  applyName;

    @ApiModelProperty(value = "申请人手机号")
    private String applyPhoneNumber;

    @ApiModelProperty(value = "乘车人")
    private String passengerName;

    @ApiModelProperty(value = "同行人")
    private String peerName;

    @ApiModelProperty(value = "开始时间",notes = "实际用车时间")
    private String beginTime;

    @ApiModelProperty(value = "结束时间",notes = "实际下车时间")
    private String endTime;

    @ApiModelProperty(value = "上车地址",notes = "实际上车地址")
    private String beginAddress;

    @ApiModelProperty(value = "下车地址",notes = "实际下车地址")
    private String endAddress;

    @ApiModelProperty(value = "服务类型 ",notes = "\\n\" +\n" +
            "            \"1000预约\\n\" +\n" +
            "            \"2001接机\\n\" +\n" +
            "            \"2002送机\\n\" +\n" +
            "            \"3000包车")
    private String serviceType;

    @ApiModelProperty(value = "包车类型",notes = "T000  非包车\n" +
            "T001 半日租（4小时）\n" +
            "T002 整日租（8小时）")
    private  String charterCarType;

    @ApiModelProperty(value = "用车方式",notes = "自有车   W100\n" +
            "网约车   W200")
    private  String useCarMode;

    @ApiModelProperty(value = "订单id")
    private String orderId;

    @ApiModelProperty(value = "费用合计")
    private String amount;

    @ApiModelProperty(value = "用车时间-前置",notes = "yyyy-mm-dd hh24:mi:ss")
    private String useCarTimeBegin;

    @ApiModelProperty(value = "用车时间-后置",notes = "yyyy-mm-dd hh24:mi:ss")
    private String  useCarTimeEnd;
    @ApiModelProperty(value = "订单状态")
    private String orderState;
    @ApiModelProperty(value = "订单编号")
    private String orderNumber;

}
