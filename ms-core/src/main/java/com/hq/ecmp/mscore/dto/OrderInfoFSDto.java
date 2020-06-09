package com.hq.ecmp.mscore.dto;


import lombok.Data;

import java.util.Date;

@Data
public class OrderInfoFSDto extends Page{

    Long orderId;

    String deptName;

    String nickName;

    String name;

    String DriverName;

    String carLicense;

    Date beginActionTime;

    Date endActionTime;

    String charterCarType;

    Long companyId;

    Long orderNumber;

    Long userId;

    String state;

    String mobile;

}
