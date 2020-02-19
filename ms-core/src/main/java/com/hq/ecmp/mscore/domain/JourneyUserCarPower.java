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
@TableName("journey_user_car_power")
public class JourneyUserCarPower extends BaseEntity<JourneyUserCarPower> {

    private static final long serialVersionUID=1L;

    @TableId(value = "power_id", type = IdType.AUTO)
    private Long powerId;

    private Long applyId;

    private Long nodeId;

    private Long journeyId;

    private String state;

    private String type;

    private String itIsReturn;


    public static final String POWER_ID = "power_id";

    public static final String APPLY_ID = "apply_id";

    public static final String NODE_ID = "node_id";

    public static final String JOURNEY_ID = "journey_id";

    public static final String STATE = "state";

    public static final String TYPE = "type";

    public static final String IT_IS_RETURN = "it_is_return";

    @Override
    protected Serializable pkVal() {
        return this.powerId;
    }

}
