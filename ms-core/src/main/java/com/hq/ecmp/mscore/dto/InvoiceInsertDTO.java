package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
@ApiModel(description = "发票新增模型")
public class InvoiceInsertDTO {
       @ApiModelProperty(value = "发票ID")
       private Long invoiceId;

       @ApiModelProperty(value = "发票类型")
       private String type;

       @ApiModelProperty(value = "发票抬头")
       private String header;

       @ApiModelProperty(value = "纳税人识别号")
       private String tin;

       @ApiModelProperty(value = "发票内容")
       private String content;

       @ApiModelProperty(value = "金额")
       private String amount;

       @ApiModelProperty(value = "收货地址")
       private String acceptAddress;

       @ApiModelProperty(value = "创建时间")
       private Date createTime;

       @ApiModelProperty(value = "状态")
       private String status;




}
