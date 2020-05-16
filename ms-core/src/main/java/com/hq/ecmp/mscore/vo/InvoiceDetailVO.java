package com.hq.ecmp.mscore.vo;

import com.hq.ecmp.mscore.domain.OrderInvoiceInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 * @Date: 2020/3/13 14:31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDetailVO {

    @ApiModelProperty(value = "申请日期")
    private String createTime;

    @ApiModelProperty(value = "开票状态")
    private String status;

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

    @ApiModelProperty(name = "periods", value = "账期列表")
    private List<PeriodsVO> periodsList;

    private List<OrderInvoiceInfo> periods;



    String bankName;

    String bankCardNo;

    String telephone;

    String email;

    String registedAddress;




}
