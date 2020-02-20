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
@TableName("car_maintenance_info")
public class CarMaintenanceInfo extends MicBaseEntity<CarMaintenanceInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "maintenance_id", type = IdType.AUTO)
    private Integer maintenanceId;

    private Long carId;

    private String maintenanceAddress;

    private BigDecimal price;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime nextMaintenanceDate;


    public static final String MAINTENANCE_ID = "maintenance_id";

    public static final String CAR_ID = "car_id";

    public static final String MAINTENANCE_ADDRESS = "maintenance_address";

    public static final String PRICE = "price";

    public static final String NEXT_MAINTENANCE_DATE = "next_maintenance_date";

    @Override
    protected Serializable pkVal() {
        return this.maintenanceId;
    }

}
