package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author
 * @date 2020/3/7
 * ecmp-proxy.
 */
@Data
@ApiModel(description = "发票收货地址信息")
public class InvoiceAddVO {
    @ApiModelProperty(name = "addressId",value = "收货信息Id")
    private String addressId;

    @ApiModelProperty(name = "accepter",value = "收件人名称")
    private String accepter;

    @ApiModelProperty(name = "mobile",value = "联系电话")
    private String mobile;

    @ApiModelProperty(name = "address",value = "收获地址")
    private String address;



}
