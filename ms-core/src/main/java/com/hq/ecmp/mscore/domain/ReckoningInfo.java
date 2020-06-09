package com.hq.ecmp.mscore.domain;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ReckoningInfo implements Serializable {

    private static final long serialVersionUID = 1472963895939550637L;

    /** 结算单标识 */
    private String offId;

    /** 用车单位 */
    private Long ecmpId;

    /** 开始年月 */
    private Date startDate;

    /** 结束年月 */
    private Date  endDate;

    /** 付款截止日期 */
    private Date  offDate;

    /** 创建时间 */
    private Date CreatDate;

    /** 状态 */
    private int status;

    /** 确认人 */
    private Long sureUser;

    /** 申请人id */
    private Long applyUserId;

}
