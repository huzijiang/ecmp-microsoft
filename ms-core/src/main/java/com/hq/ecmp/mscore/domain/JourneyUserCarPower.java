package com.hq.ecmp.mscore.domain;
/**update2**/

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.hq.ecmp.mscore.domain.base.MicBaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
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
public class JourneyUserCarPower extends MicBaseEntity<JourneyUserCarPower> {

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
