package com.hq.ecmp.mscore.dto;

import com.hq.ecmp.mscore.vo.TravelRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: zj.hu
 * @Date: 2020-01-03 17:41
 */
@Data
public class JourneyCommitApplyDto {

    /**
     * 申请单编号
     *//*
    @ApiParam(required = true)
    @NotEmpty
    @NotNull
    private Long applyId;
    *//**
     * 行程单编号
     *//*
    @ApiParam(required = true)
    @NotEmpty
    @NotNull
    private Long jouneyId;*/
    /**
     * 公务申请提交参数
     */
    @ApiModelProperty(name = "applyOfficialRequest", value = "基本申请信息",required = true, position = 1)
    private ApplyOfficialRequest applyOfficialRequest;

    /**
     * 行程信息，集合
     */
    @ApiModelProperty(name = "travelVOS", value = "差旅行程信息",required = false, position = 2)
    private List<TravelRequest> travelRequests;      //TODO 新增的
    /**
     * 出差总天数
     */
    @ApiModelProperty(name = "travelCount", value = "出差总天数",required = false, position = 3)
    private Integer travelCount;
}
