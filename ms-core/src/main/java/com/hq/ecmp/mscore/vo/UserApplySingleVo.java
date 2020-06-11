package com.hq.ecmp.mscore.vo;

import com.hq.ecmp.mscore.dto.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 用车申请
 */
@Data
@ApiModel(value = "用车申请model")
public class UserApplySingleVo extends PageRequest {

    @ApiModelProperty(value = "当前用户id")
    private Long userId;
    @ApiModelProperty(value = "当前用户id")
    private Long deptId;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

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

    @ApiModelProperty(value = "同行人数")
    private String peerNumber;

    @ApiModelProperty(value = "用车单位")
    private String vehicleUserCompany;

    private String useCarMode;

    @ApiModelProperty(value = "订单编号")
    private String orderNumber;
    @ApiModelProperty(value = "订单编号")
    private String applyNumber;

    @ApiModelProperty(value = "费用合计")
    private String amount;

    @ApiModelProperty(value = "包车类型")
    private String charterCarType;

    @ApiModelProperty(value = "服务车型")
    private String carLevel;

    @ApiModelProperty(value = "下单时间")
    private String createTime;
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

    @ApiModelProperty(value = "用车事由")
    private String  vehicleUseReason ;

    @ApiModelProperty(value = "用车备注")
    private String  vehicleRemarks;

    @ApiModelProperty(value = "用车方式")
    private String  vehicleStyle;

    @ApiModelProperty(value = "待确认状态")
    private String  homePageToBeConfirmedState;

    @ApiModelProperty(value = "待派车状态")
    private String  homePageWaitingCarState;

    @ApiModelProperty(value = "已派车状态")
    private String  homePageUsingCarState;

    @ApiModelProperty(value = "已过期状态")
    private String  homePageExpireCarState;

    @ApiModelProperty(value = "已驳回状态")
    private String  homePageRejectState;

    @ApiModelProperty(name = "carColor",value = "车辆颜色")
    private String carColor;

    @ApiModelProperty(name = "carLicense",value = "车牌号")
    private String carLicense;

    @ApiModelProperty(name = "carType",value = "车辆类型")
    private String carType;

    @ApiModelProperty(value = "车辆归属")
    private String carCompany;

    @ApiModelProperty(value = "驾驶员姓名")
    private String driverName;

    @ApiModelProperty(value = "驾驶员手机号")
    private String driverPhone;

    @ApiModelProperty(value = "驾驶员归属")
    private String driverCompany;

    @ApiModelProperty(value = "用车时长")
    private String useTime;

    @ApiModelProperty(value = "等待时长")
    private String waitingTime;

    @ApiModelProperty(value = "首页订单动态开始时间")
    private String  homeDynamicBeginTime;

    @ApiModelProperty(value = "首页订单动态结束时间")
    private String  homeDynamicEndTime;

    @ApiModelProperty(value = "内部车队名称")
    private String outerCarGroupName;

    @ApiModelProperty(value = "是否自驾")
    private String itIsSelfDriver;
    @ApiModelProperty(value = "是否可以修改")
    private int canUpdateDetail;

    @ApiModelProperty(value = "多个下车地点")
    private String addressInfo;
    private String safeRemind;
    private String labelState;
    private String state;
    private int stateFlag;
    @ApiModelProperty(value = "自驾的费用详情")
    private List<OtherCostBean> orderFees;

}
