package com.hq.ecmp.mscore.dto.lease;

import lombok.Data;

import java.util.Date;

@Data
public class LeaseSettlementDto {

    Long collectionId;

    String deptName;

    Date beginData;

    Date endDate;

    String state;

    String verifier;

    Date createTim;

}
