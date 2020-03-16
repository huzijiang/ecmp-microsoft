package com.hq.ecmp.mscore.vo;

import lombok.Data;

import java.util.List;

/**
 *
 * 审批人对象
 * @date: 2020/2/28 16:40
 * @author:guojin
 */

@Data
public class ApprovalListVO {


    /**
     * 审批人Id(审批编号)
     */

    private Long applyId;
    private String type;//申请人/审批人
    private List<ApprovalInfoVO> list;

    public ApprovalListVO() {
    }

    public ApprovalListVO(Long applyId, String type, List<ApprovalInfoVO> list) {
        this.applyId = applyId;
        this.type = type;
        this.list = list;
    }
}
