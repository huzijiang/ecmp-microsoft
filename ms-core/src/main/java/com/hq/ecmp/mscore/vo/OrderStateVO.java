package com.hq.ecmp.mscore.vo;

import lombok.Data;

/**
 *
 * 订单状态
 * @date: 2020/3/12 16:40
 * @author:caobj
 */

@Data
public class OrderStateVO {


    /**
     * 审批人Id
     */
    private Long orderId;
    private String state;
    private String labelState;
    private String startAddress;
    private String endAddress;
    private String startLongitude;
    private String startLatitude;
    private String endLatitude;//纬度
    private String endLongitude;//经度

}
