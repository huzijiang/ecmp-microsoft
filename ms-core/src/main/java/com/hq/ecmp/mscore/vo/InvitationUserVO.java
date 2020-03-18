package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 用户邀请信息
 * @author shixin
 * @date 2020/3/17
 */
@Data
@ApiModel(description = "用户邀请列表")
public class InvitationUserVO {

    /**
     * 链接名称
     */
    private String yqName;
    /**
     * 企业名称
     */
    private String companyName;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 用车制度
     */
    private String regimeUser;
    /**
     * 已通过人数
     */
    private String amountPass;
    /**
     * 待审核人数
     */
    private String amountWait;
}
