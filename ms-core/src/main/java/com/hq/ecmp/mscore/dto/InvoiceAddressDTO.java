package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("发票地址信息")
public class InvoiceAddressDTO {
  //  @ApiModelProperty(value = "发票地址Id")
 //   private String addressId;
    @ApiModelProperty(value = "收货地址")
    private String address;
    @ApiModelProperty(value = "收件人")
    private String accepter;
    @ApiModelProperty(value = "联系电话")
    private String mobile;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;



}
