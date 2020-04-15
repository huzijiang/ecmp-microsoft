package com.hq.ecmp.mscore.vo;

import lombok.Data;

/**
 * @author xueyong
 * @date 2020/1/8
 * ecmp-proxy.
 */
@Data
public class BillVO {

    /**
     * 行程里程
     */
    private Long mileage;

    /**
     * 行驶时长
     */
    private Long travelTime;

    /**
     * 订单总金额
     */
    private Long totalAmount;
}
