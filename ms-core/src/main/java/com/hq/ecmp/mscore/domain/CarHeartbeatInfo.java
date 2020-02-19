package com.hq.ecmp.mscore.domain;
/**update2**/

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.hq.ecmp.mscore.domain.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("car_heartbeat_info")
public class CarHeartbeatInfo extends BaseEntity<CarHeartbeatInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "car_heartbeat_id", type = IdType.AUTO)
    private String carHeartbeatId;

    @TableField("Attribute9")
    private String Attribute9;


    public static final String CAR_HEARTBEAT_ID = "car_heartbeat_id";

    public static final String ATTRIBUTE9 = "Attribute9";

    @Override
    protected Serializable pkVal() {
        return this.carHeartbeatId;
    }

}
