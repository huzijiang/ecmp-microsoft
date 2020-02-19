package com.hq.ecmp.mscore.domain;
/**update2**/

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.hq.ecmp.mscore.domain.base.BaseEntity;
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
@TableName("driver_heartbeat_info")
public class DriverHeartbeatInfo extends BaseEntity<DriverHeartbeatInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "heart_id", type = IdType.AUTO)
    private Long heartId;

    private Long driverId;

    private Long orderId;

    private BigDecimal longitude;

    private BigDecimal latitude;


    public static final String HEART_ID = "heart_id";

    public static final String DRIVER_ID = "driver_id";

    public static final String ORDER_ID = "order_id";

    public static final String LONGITUDE = "longitude";

    public static final String LATITUDE = "latitude";

    @Override
    protected Serializable pkVal() {
        return this.heartId;
    }

}
