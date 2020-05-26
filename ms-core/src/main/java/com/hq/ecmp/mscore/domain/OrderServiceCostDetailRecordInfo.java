package com.hq.ecmp.mscore.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class OrderServiceCostDetailRecordInfo {

    Long recordId;

    Long orderId;

    BigDecimal startLongitude;

    String startLongitudeAddress;

    BigDecimal startLatitude;

    String startLatitudeAddress;

    BigDecimal endLongitude;

    String endLongitudeAddress;

    BigDecimal endLatitude;

    String endLatitudeAddress;

    Date startTime;

    Date endTime;

    BigDecimal mileage;

    BigDecimal setMealCost;

    BigDecimal setMealMileage;

    int setMealTimes;

    BigDecimal beyondMileage;

    int beyondTime;

    BigDecimal roadAndBridgeFee;

    BigDecimal highwayTollFee;

    BigDecimal stopCarFee;

    BigDecimal accommodationFee;

    BigDecimal foodFee;

    BigDecimal othersFee;

    BigDecimal totalFee;

    BigDecimal beyondMileageFee;

    BigDecimal beyondTimeFee;

    Long createBy;

    Date createTime;

    Long updateBy;

    Date updateTime;

    List<OrderServiceImagesInfo> imageList;

    List<DriverHeartbeatInfo> heartbeatList;

    public OrderServiceCostDetailRecordInfo() {
    }
}
