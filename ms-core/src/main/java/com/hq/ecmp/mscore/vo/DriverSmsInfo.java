package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aspectj.weaver.patterns.Declare;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(description = "司机端短信模板")
public class DriverSmsInfo {

    @ApiModelProperty(name = "startDate",value = "開始時間")
    private String startDate;
    @ApiModelProperty(name = "endDate",value = "结束时间")
    private String endDate;
    @ApiModelProperty(name = "totalFee",value = "费用结算")
    private BigDecimal totalFee;
    @ApiModelProperty(name = "orderNumber",value = "订单号")
    private String orderNumber;
    @ApiModelProperty(name = "carLicense",value = "车牌号")
    private String carLicense;
    @ApiModelProperty(name = "carType",value = "车类型")
    private String carType;
    @ApiModelProperty(name = "driverName",value = "司机名")
    private String driverName;
    @ApiModelProperty(name = "mobile",value = "司机电话")
    private String mobile;
    @ApiModelProperty(name = "carGroupName",value = "车队名")
    private String carGroupName;
    @ApiModelProperty(name = "telephone",value = "车队电话")
    private String telephone;
    @ApiModelProperty(name = "dispatcherNickName",value = "司机名称")
    private String dispatcherNickName;
    @ApiModelProperty(name = "dispatcherPhoneNumber",value = "司机电话")
    private String dispatcherPhoneNumber;
    private String riderMobile;
    private String  applyMobile;

}
