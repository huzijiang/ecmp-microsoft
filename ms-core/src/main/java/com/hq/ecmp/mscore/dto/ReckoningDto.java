package com.hq.ecmp.mscore.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 收款
 */
@Data
public class ReckoningDto implements Serializable {


    private static final long serialVersionUID = 6417025431759105175L;

    //收账标识
    private Long collectionId;

    /** 开始年月 */
    private String startDate;

    /** 结束年月 */
    private String endDate;

    /** 付款截止日期 */
    private String offDate;

    /** 收款状态 */
    private String status;

    private Long userId;

    /** 公司id */
    private Long companyId;

    /** 起始页 */
    private String pageIndex;

    /** 显示页数 */
    private String pageSize;

    /** 确认人 */
    private String verifier;

    /** 车队id*/
    private Long carGroupId;
    //收账标识
    private Long collectionNumber;

    /** 1:详情 0：预览*/
    private int type;

}
