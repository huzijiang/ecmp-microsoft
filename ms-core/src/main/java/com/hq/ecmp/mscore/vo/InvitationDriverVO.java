package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户邀请信息
 * @author shixin
 * @date 2020/3/17
 */
@Data
@ApiModel(description = "用户邀请列表")
public class InvitationDriverVO {

    /**
     * 链接名称
     */
    @ApiModelProperty(name = "yqName", value = "链接名称")
    private String yqName;
    /**
     * 企业名称
     */
    @ApiModelProperty(name = "companyName", value = "企业名称")
    private String companyName;
    /**
     * 车队名称
     */
    @ApiModelProperty(name = "carGroupName", value = "车队名称")
    private String carGroupName;
    /**
     * 可用车辆数
     */
    @ApiModelProperty(name = "carNum", value = "可用车辆数")
    private String carNum;
    /**
     * 已通过人数
     */
    @ApiModelProperty(name = "amountPass", value = "已通过人数")
    private String amountPass;
    /**
     * 待审核人数
     */
    @ApiModelProperty(name = "amountWait", value = "待审核人数")
    private String amountWait;

    @ApiModelProperty(name = "state", value = "状态")
    private String state;

    @ApiModelProperty(name = "invitationId", value = "邀请ID")
    private Long invitationId;

    @ApiModelProperty(name = "stateValue", value = "状态value")
    private String stateValue;

    @ApiModelProperty(name = "roseId", value = "角色id")
    private Long roseId;

    @ApiModelProperty(name = "roseName", value = "角色Name")
    private String roseName;

}
