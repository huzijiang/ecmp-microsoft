package com.hq.ecmp.ms.api.dto.car;

import lombok.Data;

/**
 *
 * 审批人对象
 * @date: 2020/2/28 16:40
 * @author:guojin
 */

@Data
public class ApprovalVO {


    /**
     * 审批人Id
     */
    private Integer approvalId;
    /**
     * 审批人姓名
     */
    private String approvalName;
    /**
     * 审批人电话
     */
    private String approvalPhone;

    public ApprovalVO() {
    }

    public ApprovalVO(Integer approvalId, String approvalName, String approvalPhone) {
        this.approvalId = approvalId;
        this.approvalName = approvalName;
        this.approvalPhone = approvalPhone;
    }
}
