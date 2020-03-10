package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/5 11:10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplyDetailVO {

    /**
     * 申请ID
     */
    private Integer applyId;
    /**
     * 用车制度对象
     */
    //   private RegulationVO regulationVO;   //TODO 这个用不着吧
    /**
     * 申请名称（如果是差旅 eg显示城市的顺序）
     */
    @ApiModelProperty(name = "reason",value = "申请原因")
    private String reason;
    /**
     * 状态
     */
    @ApiModelProperty(name = "status",value = "申请状态")
    private String status;  //TODO Integer改动
    /**
     * 用车时间
     */
    @ApiModelProperty(name = "applyDate",value = "用车时间")
    private Date applyDate;

    /**
     * 申请类型 eg：公务、差旅
     */
    @ApiModelProperty(name = "type",value = "申请类型 eg：公务、差旅")
    private String type;  //TODO Integer改动

    /**
     * 申请人
     */
    private String applyUser;  //TODO UserVO改动
    private String applyMobile;//申请人手机
    /**
     * 乘车人
     */
    private String riderName;  //TODO UserVO改动

    /**
     * 同行人（多个，分隔）
     */
    @ApiModelProperty(name = "partner",value = "同行人（多个，分隔）")
    private String partner;

    /**
     * 地址
     */
    private String startAddress;  //TODO AddressVO addressVO改动

    /**
     * 地址
     */
    private String endAddress;   //TODO 新增
    /**
     * 是否往返
     */
    @ApiModelProperty(name = "isGoBack",value = "是否往返")
    private String isGoBack;
    /**
     * 等待时长（单位秒）
     */
    @ApiModelProperty(name = "waitingTime",value = "等待时长") //TODO 等待时长（单位秒）改动
    private String waitingTime;   //TODO Long改动
    /**
     * 预估价格（区间值，根据用车制度中的车型找到最大和最小的2个值 eg:120-400）
     */
    @ApiModelProperty(name = "estimatePrice",value = "预估价格（区间值，根据用车制度中的车型找到最大和最小的2个值 eg:120-400）")
    private String estimatePrice;
    /**
     * 成本中心
     */
    @ApiModelProperty(name = "costCenter",value = "成本中心")
    private String costCenter;
    /**
     * 项目编号
     */
    @ApiModelProperty(name = "projectNumber",value = "项目编号")
    private String projectNumber;
    /**
     * 拒绝原因
     */
    @ApiModelProperty(name = "rejectReason",value = "拒绝原因")
    private String rejectReason;

    /**
     * 审批流
     */
   // private List<ApprovalFlow> flowList;  //TODO 单独接口查

    /**
     * 申请单包含的用车权限
     */
    //  private List<TicketVO> tickets;  TODO 这个用不着吧

    /**
     * 行程描述（仅差旅申请）
     */
    private TripDescription tripDescription;



}
