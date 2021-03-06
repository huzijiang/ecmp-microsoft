package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xueyong
 * @date 2020/1/4
 * ecmp-proxy.
 */
@Data
@ApiModel(description = "任务详情")
public class DriverOrderInfoVO {

    private Long orderId;
    private Long journeyId;
    private Long userId;
    private Long driverId;
    @ApiModelProperty(name = "name",value = "姓名")
    private String userName;
    @ApiModelProperty(name = "phone",value = "电话")
    private String userPhone;
    @ApiModelProperty(name = "serviceType",value = "1000预约 2001接机 2002送机 3000包车")
    private String serviceType;
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
    @ApiModelProperty(name = "state",value = "订单状态")
    private String labelState;
    @ApiModelProperty(name = "carGroupPhone",value = "车队电话")
    private String carGroupPhone;
    @ApiModelProperty(name = "customerServicePhone",value = "客服电话")
    private String customerServicePhone;
    @ApiModelProperty(name = "useCarTime",value = "用车时间")
    private Date useCarTime;
    @ApiModelProperty(name = "endTime",value = "结束时间")
    private Date endTime;
    @ApiModelProperty(name = "flightNumber",value = "航班号")
    private String flightNumber;
    @ApiModelProperty(name = "charterCarType",value = "T001 半日租（4小时）T002 整日租（8小时）")
    private String charterCarType;
    @ApiModelProperty(name = "itIsReturn",value = "是否往返")
    private String itIsReturn;
    @ApiModelProperty(name = "startAddr",value = "上车地址")
    private String startAddr;
    @ApiModelProperty(name = "endAddr",value = "下车地址")
    private String endAddr;
    @ApiModelProperty(name = "halfway",value = "途径地")
    private String halfway;
    @ApiModelProperty(name = "peopleCount",value = "乘车人数")
    private  String peopleCount;
    @ApiModelProperty(name = "orderAmount",value = "订单费用")
    private String orderAmount;
    @ApiModelProperty(name = "orderFees",value = "费用")
    private List<OtherCostBean> orderFees;
    private List<String> feeImageUrls;



    @ApiModelProperty(name = "useTime",value = "用车总天数xmy")
    private String useTime;
    @ApiModelProperty(name = "StartDate",value = "用车开始时间xmy")
    private String startDate;
    @ApiModelProperty(name = "endDate",value = "用车结束时间xmy")
    private String endDate;
    @ApiModelProperty(name = "deptName",value = "车辆归属公司xmy")
    private String deptName;
    @ApiModelProperty(name = "groupPhone",value = "车辆队电话xmy")
    private String groupPhone;
    @ApiModelProperty(name = "driverPhone",value = "司机电话xmy")
    private String driverPhone;
    @ApiModelProperty(name = "peerNumber",value = "同乘人数")
    private String peerNumber;
    @ApiModelProperty(name = "imageUrl",value = "车辆信息图")
    private String imageUrl;
    @ApiModelProperty(name = "totalMileage",value = "总里程")
    private String totalMileage;
    @ApiModelProperty(name = "totalTime",value = "总时长")
    private String totalTime;
    private Date labelStateOptTime;
    private String labelStateContent;

    /**
     * 用车人归属单位（实际是订单申请人 的信息）
     */
    @ApiModelProperty(name = "userOwnerOrg",value = "申请人归属单位")
    private String userOwnerOrg;



}
