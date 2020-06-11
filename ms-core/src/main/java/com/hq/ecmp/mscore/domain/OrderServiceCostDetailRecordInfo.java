package com.hq.ecmp.mscore.domain;

import com.hq.ecmp.mscore.mapper.OrderInfoMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class OrderServiceCostDetailRecordInfo {

    Long recordId;

    Long orderId;

    String startLatitudeAddress;

    String endLatitudeAddress;

    String startLongitudeAddress;

    String endLongitudeAddress;

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

    BigDecimal totalFee;

    BigDecimal beyondMileageFee;

    BigDecimal beyondTimeFee;

    Long createBy;

    Date createTime;

    Long updateBy;

    Date updateTime;

    String startAddress;

    String endAddress;

    List<OrderServiceImagesInfo> imageList;

    List<DriverHeartbeatInfo> heartbeatList;
    @Autowired
    private OrderInfoMapper orderInfoMapper;

    public OrderServiceCostDetailRecordInfo() {
    }

    public Map<String, Object> orderServiceCategory() {
        return orderInfoMapper.orderServiceCategory();
    }
}
