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
@TableName("car_location_info")
public class CarLocationInfo extends MicBaseEntity<CarLocationInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "car_location_id", type = IdType.AUTO)
    private String carLocationId;

    private Long carId;


    public static final String CAR_LOCATION_ID = "car_location_id";

    public static final String CAR_ID = "car_id";

    @Override
    protected Serializable pkVal() {
        return this.carLocationId;
    }

}
