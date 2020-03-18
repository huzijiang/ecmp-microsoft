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
public class ApprovaReesultVO {


    /**
     * 审批人Id
     */
    private Long approveResultId;
    private Long approvalNodeId;
    private Long applyId;
    private Long jouneyId;
    //审批结果
    private String approveResult;
    //审批状态
    private String state;
    //用车原因
    private String reason;
    private String content;
    private String title;
    //申请人
    private String applyName;
    //行程安排
    private String stroke;
    //用车时间
    private String useCarTime;
    //申请时间
    private Date applyTime;
    private String startAddress;
    private String endAddress;
    //用车类型(差旅还是公务)
    private String applyType;
    //是否往返
    private String itIsReturn;

    public ApprovaReesultVO() {

    }


}
