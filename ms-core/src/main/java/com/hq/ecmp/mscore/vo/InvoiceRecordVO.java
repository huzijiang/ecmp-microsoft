package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "发票记录列表")
public class InvoiceRecordVO {

    @ApiModelProperty(value = "申请日期")
    private String createTime;

    @ApiModelProperty(value = "发票ID")
    private Long invoiceId;

    @ApiModelProperty(value = "发票抬头")
    private String header;

    @ApiModelProperty(value = "金额")
    private String amount;

    @ApiModelProperty(value = "发票内容")
    private String content;

    @ApiModelProperty(value = "收货信息")
    private String acceptAddress;

    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "发票类型")
    private String type;
    @ApiModelProperty(value = "纳税号")
    private String tin;
    @ApiModelProperty(value = "发票url")
    private String invoiceUrl;

    String  createBy;

    String bankName;

    String bankCardNo;

    String telephone;

    String email;

    String registedAddress;
}
