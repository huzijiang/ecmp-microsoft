package com.hq.ecmp.mscore.domain;
/**update2**/

import java.math.BigDecimal;
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
@TableName("journey_plan_price_info")
public class JourneyPlanPriceInfo extends MicBaseEntity<JourneyPlanPriceInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "price_id", type = IdType.AUTO)
    private Long priceId;

    private Long journeyId;

    private Long nodeId;

    private Long carTypeId;

    private BigDecimal price;


    public static final String PRICE_ID = "price_id";

    public static final String JOURNEY_ID = "journey_id";

    public static final String NODE_ID = "node_id";

    public static final String CAR_TYPE_ID = "car_type_id";

    public static final String PRICE = "price";

    @Override
    protected Serializable pkVal() {
        return this.priceId;
    }

}
