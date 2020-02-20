package com.hq.ecmp.mscore.domain;
/**update2**/

import com.baomidou.mybatisplus.annotation.TableName;
import com.hq.ecmp.mscore.domain.base.MicBaseEntity;

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
@TableName("driver_car_relation_info")
public class DriverCarRelationInfo extends MicBaseEntity<DriverCarRelationInfo> {

    private static final long serialVersionUID=1L;

    private Long userId;

    private Long driverId;

    private Long carId;


    public static final String USER_ID = "user_id";

    public static final String DRIVER_ID = "driver_id";

    public static final String CAR_ID = "car_id";

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
