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
@TableName("order_state_trace_info")
public class OrderStateTraceInfo extends BaseEntity<OrderStateTraceInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "trace_id", type = IdType.AUTO)
    private Long traceId;

    private Long orderId;

    private String state;

    private BigDecimal driverLongitude;

    private BigDecimal driverLatitude;

    private String content;


    public static final String TRACE_ID = "trace_id";

    public static final String ORDER_ID = "order_id";

    public static final String STATE = "state";

    public static final String DRIVER_LONGITUDE = "driver_longitude";

    public static final String DRIVER_LATITUDE = "driver_latitude";

    public static final String CONTENT = "content";

    @Override
    protected Serializable pkVal() {
        return this.traceId;
    }

}
