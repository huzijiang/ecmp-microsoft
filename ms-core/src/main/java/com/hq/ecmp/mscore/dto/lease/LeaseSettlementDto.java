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

    String verifier;

    Date createTime;

    Long createBy;

    Long companyId;

    String carGroupId;

}
