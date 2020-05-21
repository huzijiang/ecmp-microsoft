package com.hq.ecmp.mscore.domain;


import com.hq.core.web.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
public class DriverNatureInfo extends BaseEntity {

    Long driverId;

    String driverNature;

    Date hireBeginTime;

    Date hireEndTime;

    Date borrowBeginTime;

    Date borrowEndTime;
}
