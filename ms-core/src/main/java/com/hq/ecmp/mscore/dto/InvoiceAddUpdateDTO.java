package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("发票地址信息")
public class InvoiceAddUpdateDTO {
    @ApiModelProperty(value = "发票地址Id")
    private Long addressId;
    @ApiModelProperty(value = "收货地址")
    private String address;
    @ApiModelProperty(value = "收件人")
    private String accepter;
    @ApiModelProperty(value = "联系电话")
    private String mobile;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
