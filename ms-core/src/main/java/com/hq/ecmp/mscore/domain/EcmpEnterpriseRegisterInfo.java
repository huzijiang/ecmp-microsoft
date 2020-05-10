package com.hq.ecmp.mscore.domain;

import java.util.Date;
import com.hq.core.web.domain.BaseEntity;
import lombok.Data;

/**
 * (EcmpEnterpriseRegisterInfo)实体类
 *
 * @author makejava
 * @since 2020-03-17 18:10:01
 */
@Data
public class EcmpEnterpriseRegisterInfo extends BaseEntity{
    private static final long serialVersionUID = 1L;

    private Long registerId;

    private Long invitationId;

    private String type;

    private String name;

    private String gender;

    private String jobNumber;

    private String email;

    private String mobile;

    private String smsAuthCode;

    private String reason;

    private String idCard;
    /**
    * 车队编号
    */
    private Long carGroupId;

    private String licenseType;

    private Date licenseInitIssueDate;

    private Date licenseIssueDate;

    private Date licenseExpireDate;

    private String licenseNumber;

    private String licenseArchivesNumber;

    private String licenseImages;

    private String state;

    private Long auditor;

    private Date auditTime;
    /* 新增审核拒绝原因*/
    private String rejectReason;
}
