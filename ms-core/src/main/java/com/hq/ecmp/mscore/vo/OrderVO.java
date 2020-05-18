package com.hq.ecmp.mscore.vo;

import com.hq.ecmp.mscore.dto.OrderHistoryTraceDto;
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
@ApiModel(description = "待服务详情")
public class OrderVO {

    private Long orderId;
    private Long driverId;
    private Long cardId;
    private String orderNumber;
    private Long regimeId;
    @ApiModelProperty(name = "driverMobile",value = "司机手机")
    private String driverMobile;
    @ApiModelProperty(name = "driverName",value = "司机姓名")
    private String driverName;
    @ApiModelProperty(name = "driverScore",value = "司机评分")
    private String driverScore;
    @ApiModelProperty(name = "carColor",value = "车辆颜色")
    private String carColor;
    @ApiModelProperty(name = "carLicense",value = "车牌号")
    private String carLicense;
    @ApiModelProperty(name = "powerType",value = "车辆动力类型")
    private String powerType;
    @ApiModelProperty(name = "carType",value = "车辆类型")
    private String carType;
    @ApiModelProperty(name = "hint",value = "提示语")
    private String hint;
    @ApiModelProperty(name = "state",value = "订单状态")
    private String state;
    @ApiModelProperty(name = "carGroupPhone",value = "车队电话")
    private String carGroupPhone;
    @ApiModelProperty(name = "carGroupName",value = "车队电话")
    private String carGroupName;
    @ApiModelProperty(name = "isAddContact",value = "是否添加联系人(1是0否)")
    private String isAddContact;
    @ApiModelProperty(name = "driverType",value = "司机类型(企业驾驶员/网约驾驶员)")
    private String driverType;
    @ApiModelProperty(name = "customerServicePhone",value = "客服电话")
    private String customerServicePhone;
    @ApiModelProperty(name = "useCarTime",value = "用车时间")
    private String useCarTime;
    @ApiModelProperty(name = "createTimestamp",value = "下单时间")
    private Long createTimestamp;
    @ApiModelProperty(name = "useCarTimestamp",value = "下单时间")
    private Long useCarTimestamp;
    @ApiModelProperty(name = "duration",value = "用车时长")
    private String duration;
    @ApiModelProperty(name = "distance",value = "里程")
    private String distance;
    @ApiModelProperty(name = "amount",value = "金额")
    private String amount;
    @ApiModelProperty(name = "labelState",value = "辅助状态")
    private String labelState;
    @ApiModelProperty(name = "isDisagree",value = "是否展示异议1手动(需展示),0自动(不展示)")
    private int isDisagree;
    @ApiModelProperty(name = "score",value = "订单评分")
    private String score;
    @ApiModelProperty(name = "isVirtualPhone",value = "是否号码保护")
    private Integer isVirtualPhone;
    @ApiModelProperty(name = "orderEndTime",value = "订单结束时间")
    private String orderEndTime;
    @ApiModelProperty(name = "description",value = "订单评分描述")
    private String description;
    @ApiModelProperty(name = "cancelReason",value = "取消原因")
    private String cancelReason;
    @ApiModelProperty(name = "carPhoto",value = "车辆照片")
    private String carPhoto;
    @ApiModelProperty(name = "orderCostDetailVO",value = "费用对象")
    private OrderCostDetailVO   orderCostDetailVO;
    @ApiModelProperty(name = "isExcess",value = "是否超额:0否1超额")
    private Integer isExcess;
    @ApiModelProperty(name = "excessMoney",value = "超额金额")
    private String excessMoney;
    @ApiModelProperty(name = "payState",value = "是否支付")
    private String payState;
    @ApiModelProperty(name = "payId",value = "对外订单编号，与第三方平台对接")
    private String payId;
    @ApiModelProperty(name = "historyTraceList",value = "订单轨迹")
    private List<OrderHistoryTraceDto> historyTraceList;
    @ApiModelProperty(name = "cancelFee",value = "网约车取消费明细")
    private CancelOrderCostVO cancelFee;

    @ApiModelProperty(name = "startAddress",value = "出发地")
    private String startAddress;
    @ApiModelProperty(name = "endAddress",value = "目的地")
    private String endAddress;
}

