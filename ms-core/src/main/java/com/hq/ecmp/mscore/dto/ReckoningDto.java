package com.hq.ecmp.mscore.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 收款
 */
@Data
public class ReckoningDto implements Serializable {


    private static final long serialVersionUID = 6417025431759105175L;



    /** 用车单位 */
    private Long ecmpId;

    /** 开始年月 */
    private String startDate;

    /** 结束年月 */
    private String endDate;

    /** 付款截止日期 */
    private String offDate;

    /** 收款状态 */
    private String status;

    /** 结算标识 */
    private Long companyId;

    /** 起始页 */
    private String pageIndex;

    /** 收款状态 */
    private String pageSize;

    /** 确认人 */
    private String verifier;



}
