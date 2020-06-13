package com.hq.ecmp.mscore.dto.lease;

import com.hq.ecmp.mscore.dto.Page;
import lombok.Data;

import java.util.Date;

@Data
public class LeaseSettlementDto extends Page {

    Long collectionId;

    String deptName;

    Date beginDate;

    Date endDate;

    String state;

    Long verifier;

    String verifierName;

    Date createTime;

    Date updateTime;

    Long createBy;

    Long updateBy;

    Long companyId;

    String carGroupId;

    Date collectionEndTime;

    Long collectionNumber;

    String carGroupName;

    Long serviceOrg;
}
