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
@TableName("car_insurance_info")
public class CarInsuranceInfo extends MicBaseEntity<CarInsuranceInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "car_insurance_id", type = IdType.AUTO)
    private Integer carInsuranceId;

    private Long carId;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime insuranceBeginDate;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime insuranceEndDate;

    private BigDecimal price;


    public static final String CAR_INSURANCE_ID = "car_insurance_id";

    public static final String CAR_ID = "car_id";

    public static final String INSURANCE_BEGIN_DATE = "insurance_begin_date";

    public static final String INSURANCE_END_DATE = "insurance_end_date";

    public static final String PRICE = "price";

    @Override
    protected Serializable pkVal() {
        return this.carInsuranceId;
    }

}
