package com.hq.ecmp.mscore.vo;

import lombok.Data;

@Data
public class RejectDispatcherUserVO {
    private Long traceId;
    private Long orderId;
    private int type;//1申请人/2审批人
    private String time;//申请时间/审批通过/驳回时间
    private String userPhone;
    private String userName;
    private String content;
    private String result;
    private String state;
    private Long userId;
}
