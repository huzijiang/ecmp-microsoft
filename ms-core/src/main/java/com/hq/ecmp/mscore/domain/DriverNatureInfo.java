package com.hq.ecmp.mscore.domain;


import lombok.Data;

import java.util.Date;

@Data
public class DriverNatureInfo {

    Long driverId;

    String driverNature;

    Date hireBeginTime;

    Date hireEndTime;

    Date borrowBeginTime;

    Date borrowEndTime;
}
