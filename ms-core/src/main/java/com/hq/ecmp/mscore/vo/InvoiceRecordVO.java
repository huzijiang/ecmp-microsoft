package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "发票记录列表")
public class InvoiceRecordVO {


    @ApiModelProperty(value = "申请日期")
    private String createTime;

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


}
