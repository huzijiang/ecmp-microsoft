package com.hq.ecmp.mscore.dto.lease;

import lombok.Data;

import java.util.Date;

@Data
public class LeaseSettlementDto {

    Long collectionId;

    String deptName;

    Date beginDate;

    Date endDate;

    String state;

    String verifier;

    Date createTime;

    Long createBy;

    Long companyId;

    String carGroupId;

}
