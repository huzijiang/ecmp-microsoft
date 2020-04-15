package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 差旅审批对象
 * @date: 2020/2/29 14:41
 * @author:guojin
 */

@Data
public class TravelApprovalVO {

    @ApiModelProperty(name = "applyDetailVO",value = "申请单详情")
    private ApplyDetailVO applyDetailVO;

    @ApiModelProperty(name = "approvalVOS",value = "审批列表")
    private List<ApprovalListVO> approvalVOS;

}
