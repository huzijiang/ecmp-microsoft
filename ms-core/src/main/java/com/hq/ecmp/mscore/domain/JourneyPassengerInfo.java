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
@TableName("journey_passenger_info")
public class JourneyPassengerInfo extends MicBaseEntity<JourneyPassengerInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "journey_passenger_id", type = IdType.AUTO)
    private Long journeyPassengerId;

    private Long journeyId;

    private String name;

    private String mobile;

    private String itIsPeer;


    public static final String JOURNEY_PASSENGER_ID = "journey_passenger_id";

    public static final String JOURNEY_ID = "journey_id";

    public static final String NAME = "name";

    public static final String MOBILE = "mobile";

    public static final String IT_IS_PEER = "it_is_peer";

    @Override
    protected Serializable pkVal() {
        return this.journeyPassengerId;
    }

}
