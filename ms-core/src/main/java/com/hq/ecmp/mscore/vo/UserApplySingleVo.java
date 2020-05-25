package com.hq.ecmp.mscore.vo;

import com.hq.ecmp.mscore.dto.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用车申请
 */
@Data
@ApiModel(value = "用车申请model")
public class UserApplySingleVo extends PageRequest {

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

    @ApiModelProperty(value = "包车类型")
    private String charterCarType;
    @ApiModelProperty(value = "服务车型")
    private String carLevel;

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
}
