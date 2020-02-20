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
@TableName("car_group_driver_relation")
public class CarGroupDriverRelation extends MicBaseEntity<CarGroupDriverRelation> {

    private static final long serialVersionUID=1L;

    private Long driverId;

    private Long carGroupId;


    public static final String DRIVER_ID = "driver_id";

    public static final String CAR_GROUP_ID = "car_group_id";

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
