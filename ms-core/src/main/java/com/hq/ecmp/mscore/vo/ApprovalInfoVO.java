package com.hq.ecmp.mscore.vo;

import lombok.Data;

/**
 *
 * 审批人对象
 * @date: 2020/2/28 16:40
 * @author:guojin
 */

@Data
public class ApprovalInfoVO {


    /**
     * 审批人Id(审批编号)
     */
    private Long approveResultId;
    private Long approvalNodeId;
    /**
     * 审批人姓名
     */
    private String approval;
    /**
     * 审批人电话
     */
    private String approvalMobile;
    //审批结果
    private String approveResult;

    private String approveState;

    //驳回原因
    private String content;

    public ApprovalInfoVO() {
    }

    public ApprovalInfoVO(Long approvalNodeId, String approvalName, String approvalMobile) {
        this.approvalNodeId = approvalNodeId;
        this.approval = approvalName;
        this.approvalMobile = approvalMobile;
    }

    public ApprovalInfoVO(Long approvalNodeId, String approvalName, String approvalMobile, String approveResult, String approveState) {
        this.approvalNodeId = approvalNodeId;
        this.approval = approvalName;
        this.approvalMobile = approvalMobile;
        this.approveResult = approveResult;
        this.approveState = approveState;
    }

}
