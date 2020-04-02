package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
@ApiModel(description = "发票抬头")
public class InvoiceHeaderDTO {
       @ApiModelProperty(value = "发票抬头")
       private String header;

       @ApiModelProperty(value = "纳税人识别号")
       private String tin;

       @ApiModelProperty(value = "注册地址")
       private String registedAddress;

       @ApiModelProperty(value = "注册电话")
       private String telephone;

       @ApiModelProperty(value = "开户银行")
       private String bankName;

       @ApiModelProperty(value = "银行账号")
       private String bankCardNo;

       @ApiModelProperty(value = "创建时间")
       private Date createTime;



}
