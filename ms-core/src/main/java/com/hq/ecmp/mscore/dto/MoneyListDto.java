package com.hq.ecmp.mscore.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MoneyListDto {


    private String orderNumber;// #订单号
    private String userName;// #用车人
    private String carType;// #用车类型名
    private String carLevel;
    private Date actionTime;// #用车时间
    private String useTime;// #用车天数
    private Long orderId;
    private Double carTotalMoney;//用车总费用
    private Double otherTotalMoney;//其他总费用

    private Double amount;// #用车+其他 = 总费用
    private String amountDetai;//用车费用
    private String outPrice;//其他费用




}
