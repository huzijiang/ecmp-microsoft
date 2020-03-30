package com.hq.ecmp.mscore.dto.dispatch;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: zj.hu
 * @Date: 2020-03-20 21:04
 */
@Data
public class DispatchSelectCarDto {
    /**
     * 调度员编号-当前是谁在操作
     */
    @ApiModelProperty(name = "dispatcherId", value = "调度员编号-当前是谁在操作",required = true)
    private String dispatcherId;

    /**
     * 申请单 编号
     */
    @ApiModelProperty(name = "applyId", value = "申请单 编号",required = true)
    private String applyId;


    /**
     * 订单 编号
     */
    @ApiModelProperty(name = "orderNo", value = "订单 编号",required = true)
    private String orderNo;


    /**
     * 车型级别代码
     * P001-公务级
     * P002-行政级
     * P003-六座商务
     */
    @ApiModelProperty(name = "carModelLevelType", value = "车型级别代码")
    private String carModelLevelType;

    /**
     * 车牌号
     */
    @ApiModelProperty(name = "plateLicence", value = "车牌号-调度员输入的内容")
    private String carLicence;

    /**
     * 驾驶员 编号
     */
    @ApiModelProperty(name = "driverId", value = "驾驶员 编号,调度员选择的驾驶员编号,没有不传")
    private String driverId;
}
