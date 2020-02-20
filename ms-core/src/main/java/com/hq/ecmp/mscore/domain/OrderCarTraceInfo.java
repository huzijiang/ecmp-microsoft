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
@TableName("order_car_trace_info")
public class OrderCarTraceInfo extends MicBaseEntity<OrderCarTraceInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    private String carId;

    private String longitude;

    private String latitude;

    private String carLicense;

    private Long orderId;


    public static final String ID = "id";

    public static final String CAR_ID = "car_id";

    public static final String LONGITUDE = "longitude";

    public static final String LATITUDE = "latitude";

    public static final String CAR_LICENSE = "car_license";

    public static final String ORDER_ID = "order_id";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
