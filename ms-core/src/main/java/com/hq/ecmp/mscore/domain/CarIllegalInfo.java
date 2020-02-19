package com.hq.ecmp.mscore.domain;
/**update2**/

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
@TableName("car_illegal_info")
public class CarIllegalInfo extends BaseEntity<CarIllegalInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "illegal_id", type = IdType.AUTO)
    private Long illegalId;

    private Long carId;

    private String address;

    private String score;

    private String driverId;


    public static final String ILLEGAL_ID = "illegal_id";

    public static final String CAR_ID = "car_id";

    public static final String ADDRESS = "address";

    public static final String SCORE = "score";

    public static final String DRIVER_ID = "driver_id";

    @Override
    protected Serializable pkVal() {
        return this.illegalId;
    }

}
