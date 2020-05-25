package com.hq.ecmp.mscore.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class OrderServiceCostDetailRecordInfo {

    Long recordId;

    Long orderId;

    BigDecimal startLongitude;

    BigDecimal startLatitude;

    BigDecimal endLongitude;

    BigDecimal endLatitude;

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

    Long createBy;

    Date createTime;

    Long updateBy;

    Date updateTime;

    List<OrderServiceImagesInfo> imageList;
}
