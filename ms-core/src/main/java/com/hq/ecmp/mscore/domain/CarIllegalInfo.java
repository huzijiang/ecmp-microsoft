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
@TableName("car_illegal_info")
public class CarIllegalInfo extends MicBaseEntity<CarIllegalInfo> {

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
