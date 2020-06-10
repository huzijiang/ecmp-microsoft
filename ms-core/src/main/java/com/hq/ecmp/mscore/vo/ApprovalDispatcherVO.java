package com.hq.ecmp.mscore.vo;

import lombok.Data;

import java.util.Date;

/**
 *
 * 审批人对象
 * @date: 2020/2/28 16:40
 * @author:guojin
 */

@Data
public class ApprovalDispatcherVO {


    /**
     * 审批人Id(审批编号)
     */

    private Long applyId;
    private Long dispatchId;
    private Long carGroupId;
    private String orderNumber;
    private String approvalName;
    private String approvalMobile;
    //审批结果
    private String approveResult;
    private String approveState;
    //驳回原因
    private String content;
    private String time;
    public ApprovalDispatcherVO() {
    }

    public ApprovalDispatcherVO(Long applyId, String orderNumber, String approvalName, String approvalMobile, String approveState, String time) {
        this.applyId = applyId;
        this.orderNumber = orderNumber;
        this.approvalName = approvalName;
        this.approvalMobile = approvalMobile;
        this.approveState = approveState;
        this.time = time;
    }
    public ApprovalDispatcherVO(Long applyId, String orderNumber, String approvalName, String approvalMobile, String approveState, String time,String content) {
        this.applyId = applyId;
        this.orderNumber = orderNumber;
        this.approvalName = approvalName;
        this.approvalMobile = approvalMobile;
        this.approveState = approveState;
        this.time = time;
        this.content = content;
    }
}
