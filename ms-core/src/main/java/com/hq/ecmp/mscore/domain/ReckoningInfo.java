package com.hq.ecmp.mscore.domain;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ReckoningInfo implements Serializable {

    private static final long serialVersionUID = 1472963895939550637L;

    /** 结算单标识 */
    private String companyId;
    /** 用车单位 */
    private Long carGroupId;
    /** 开始年月 */
    private Date beginDate;
    /** 结束年月 */
    private Date endDate;
    /** 状态 */
    private String state;
    /** 付款截止日期 */
    private Date collectionEndTime;
    /** 创建时间 */
    private Date createTime;
    /** 确认人 */
    private Long verifier;
    /** 申请人id */
    private Long applicant;
    /** 创建人id */
    private Long createBy;
    /** 修改人id */
    private Long updateBy;
    /** 更新时间 */
    private Date updateTime;




}
