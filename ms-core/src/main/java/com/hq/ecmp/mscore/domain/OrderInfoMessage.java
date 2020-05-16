package com.hq.ecmp.mscore.domain;

import lombok.Data;

@Data
public class OrderInfoMessage {

    /***
     * 订单id
     */
     Long orderId;

    /***
     * 订单车型
     */
    String carModel;

    /***
     * 订单车牌号
     */
    String carLicense;

    /***
     * 驾驶员名字
     */
    String driverName;


    /***
     * 当前订单车颜色
     */
    String carColor;
}
