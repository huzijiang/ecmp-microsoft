package com.hq.ecmp.mscore.domain;
/**update2**/

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.hq.ecmp.mscore.domain.base.MicBaseEntity;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author crk
 * @since 2020-02-20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("order_info")
public class OrderInfo extends MicBaseEntity<OrderInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "order_id", type = IdType.AUTO)
    private Long orderId;

    private Long journeyId;

    private Long nodeId;

    private Long powerId;

    private Long driverId;

    private Long carId;

    private String useCarMode;

    private String state;

    private String actualSetoutAddress;

    private String actualSetoutAddressLong;

    private BigDecimal actualSetoutLongitude;

    private BigDecimal actualSetoutLatitude;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime actualSetoutTime;

    private String actualArriveAddress;

    private String actualArriveAddressLong;

    private BigDecimal actualArriveLongitude;

    private BigDecimal actualArriveLatitude;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime actualArriveTime;

    private String tripartiteOrderId;

    private String tripartitePlatformCode;

    private String driverName;

    private String driverMobile;

    private String carLicense;


    public static final String ORDER_ID = "order_id";

    public static final String JOURNEY_ID = "journey_id";

    public static final String NODE_ID = "node_id";

    public static final String POWER_ID = "power_id";

    public static final String DRIVER_ID = "driver_id";

    public static final String CAR_ID = "car_id";

    public static final String USE_CAR_MODE = "use_car_mode";

    public static final String STATE = "state";

    public static final String ACTUAL_SETOUT_ADDRESS = "actual_setout_address";

    public static final String ACTUAL_SETOUT_ADDRESS_LONG = "actual_setout_address_long";

    public static final String ACTUAL_SETOUT_LONGITUDE = "actual_setout_longitude";

    public static final String ACTUAL_SETOUT_LATITUDE = "actual_setout_latitude";

    public static final String ACTUAL_SETOUT_TIME = "actual_setout_time";

    public static final String ACTUAL_ARRIVE_ADDRESS = "actual_arrive_address";

    public static final String ACTUAL_ARRIVE_ADDRESS_LONG = "actual_arrive_address_long";

    public static final String ACTUAL_ARRIVE_LONGITUDE = "actual_arrive_longitude";

    public static final String ACTUAL_ARRIVE_LATITUDE = "actual_arrive_latitude";

    public static final String ACTUAL_ARRIVE_TIME = "actual_arrive_time";

    public static final String TRIPARTITE_ORDER_ID = "tripartite_order_id";

    public static final String TRIPARTITE_PLATFORM_CODE = "tripartite_platform_code";

    public static final String DRIVER_NAME = "driver_name";

    public static final String DRIVER_MOBILE = "driver_mobile";

    public static final String CAR_LICENSE = "car_license";

    @Override
    protected Serializable pkVal() {
        return this.orderId;
    }

}
