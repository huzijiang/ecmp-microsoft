package com.hq.ecmp.mscore.dto.dispatch;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: zj.hu
 * @Date: 2020-03-23 20:52
 */
@Data
public class DispatchCountCarAndDriverDto {
    /**
     * 订单 编号
     */
    @ApiModelProperty(name = "orderNo", value = "订单 编号",required = true)
    private String orderNo;

    /**
     * 订单 编号
     */
    @ApiModelProperty(name = "token", value = "调度员Token",required = true)
    private String token;

}
