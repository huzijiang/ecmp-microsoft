package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author xueyong
 * @date 2020/1/4
 * ecmp-proxy.
 */
@Data
@Builder
public class ApprovalFlow {

    /**
     * 审批流ID
     */
    private Integer flowId;
    /**
     * 审批操作ID
     */
    @ApiModelProperty(name = "operationId",value = "审批操作ID")
    private Integer operationId;
    /**
     * 审批人姓名
     */
    @ApiModelProperty(name = "approvalUser",value = "审批人姓名")
    private String approvalUser;
    /**
     * 审批人电话
     */
    @ApiModelProperty(name = "approvalPhone",value = "审批人电话")
    private String approvalPhone;
    /**
     * 审批状态
     */
    @ApiModelProperty(name = "status",value = "审批状态")
    private String status;
    /**
     * 审批时间
     */
    @ApiModelProperty(name = "createDate",value = "审批时间")
    private Date createDate;
}