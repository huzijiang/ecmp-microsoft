package com.hq.ecmp.mscore.vo;

import lombok.Data;

/**
 * @author xueyong
 * @date 2020/1/4
 * ecmp-proxy.
 */
@Data
public class CarInfoVO {

    /**
     * 车辆ID
     */
    private Integer carId;
    /**
     * 车辆类型 eg 公务、差旅
     */
    private String carType;
    /**
     * 车型 eg 红旗H5 红旗H7
     */
    private String carModelName;
    /**
     * 车牌号
     */
    private String plateLicence;
    /**
     * 状态
     * 可用、任务冲突、无符合驾驶员
     */
    private String status;
    /**
     * 颜色
     */
    private String color;
}
