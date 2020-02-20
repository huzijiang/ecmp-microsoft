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
@TableName("journey_info")
public class JourneyInfo extends MicBaseEntity<JourneyInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "journey_id", type = IdType.AUTO)
    private Long journeyId;

    private Long userId;

    private Long regimenId;

    private String serviceType;

    private String useCarMode;

    private String useCarTime;

    private String itIsReturn;

    private String estimatePrice;

    private Integer projectId;

    private String flightNumber;

    private String useTime;

    private String waitTimeLong;

    private String charterCarType;


    public static final String JOURNEY_ID = "journey_id";

    public static final String USER_ID = "user_id";

    public static final String REGIMEN_ID = "regimen_id";

    public static final String SERVICE_TYPE = "service_type";

    public static final String USE_CAR_MODE = "use_car_mode";

    public static final String USE_CAR_TIME = "use_car_time";

    public static final String IT_IS_RETURN = "it_is_return";

    public static final String ESTIMATE_PRICE = "estimate_price";

    public static final String PROJECT_ID = "project_id";

    public static final String FLIGHT_NUMBER = "flight_number";

    public static final String USE_TIME = "use_time";

    public static final String WAIT_TIME_LONG = "wait_time_long";

    public static final String CHARTER_CAR_TYPE = "charter_car_type";

    @Override
    protected Serializable pkVal() {
        return this.journeyId;
    }

}
