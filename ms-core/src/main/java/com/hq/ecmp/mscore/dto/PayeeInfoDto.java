package com.hq.ecmp.mscore.dto;

import lombok.Data;

@Data
public class PayeeInfoDto {

    private Long userId;
    private Long companyId;
    private Long carGroupId;
    private String carGroupName;
    private String telephone;
    private String accountName;
    private String bankName;
    private String bankAccount;
    private String deptName;
    private String collectionId;
}
