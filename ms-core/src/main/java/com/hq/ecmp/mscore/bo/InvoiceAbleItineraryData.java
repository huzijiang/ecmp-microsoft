package com.hq.ecmp.mscore.bo;


import lombok.Data;

@Data
public class InvoiceAbleItineraryData {

    /***
     * 账务id
     */
    Long accountId;


    /***
     *是否开票状态
     */
    String state ;


    /***
     * 支付金额
     */
    String amount;


    /***
     * 订单地址
     */
    String address;


    /***
     * 订单地址
     */
    String addressLong;


    /***
     * 订单生成时间
     */
    String actionTime;




}
