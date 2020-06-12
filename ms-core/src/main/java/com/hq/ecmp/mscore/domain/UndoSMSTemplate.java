package com.hq.ecmp.mscore.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 撤销短信模板
 */
@Data
@ApiModel(description = "撤销短信模板")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UndoSMSTemplate {


    @ApiModelProperty(name = "nickName", value = "申请人姓名")
    private String nickName;

    @ApiModelProperty(name = "phonenumber", value = "申请人手机号")
    private String phonenumber;

    @ApiModelProperty(name = "vehicleUser", value = "用车人姓名")
    private String vehicleUser;

    @ApiModelProperty(name = "vehicleUserMobile", value = "用车人手机号")
    private String vehicleUserMobile;

    @ApiModelProperty(name = "notes", value = "用车备注")
    private String notes;

    @ApiModelProperty(name = "useTime", value = "用车天数")
    private String useTime;

    @ApiModelProperty(name = "startDate", value = "用车时间")
    private Date startDate;

    @ApiModelProperty(name = "state", value = "订单状态")
    private String state;

    @ApiModelProperty(name = "companyId", value = "公司id")
    private Long companyId;

    @ApiModelProperty(name = "innerPhonenumber", value = "内部调度员手机号")
    private String innerPhonenumber;

    @ApiModelProperty(name = "outerPhonenumber", value = "外部调度员手机号")
    private String outerPhonenumber;

    @ApiModelProperty(name = "driverMobile", value = "驾驶员手机号")
    private String driverMobile;

    @ApiModelProperty(name = "orderNumber", value = "订单编号")
    private String orderNumber;

    @ApiModelProperty(name = "deptName", value = "申请人单位名称")
    private String deptName;

    @ApiModelProperty(name = "carTypeName", value = "服务车型")
    private String carTypeName;

    @ApiModelProperty(name = "orderId", value = "订单id")
    private Long orderId;
}
