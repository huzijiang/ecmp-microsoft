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
    private String dispatcherId;

    /**
     * 订单 编号
     */
    private String orderNo;

    /**
     * 车型级别代码,不传默认全部
     *
     * P001-公务级
     * P002-行政级
     * P003-六座商务
     */
    private String carModelLevelType;

    /**
     * 车牌号
     */
    private String plateLicence;

    /**
     * 驾驶员 编号
     */
    private String driverId;

    /**
     * 车型信息
     *   包含品牌、车系
     */
    private String carTypeInfo;

}
