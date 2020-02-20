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
@TableName("car_yearly_check_info")
public class CarYearlyCheckInfo extends MicBaseEntity<CarYearlyCheckInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "yearly_check_id", type = IdType.AUTO)
    private Long yearlyCheckId;

    private Long carId;

    private String yearCheckNextDate;

    private String yearCheckState;

    private String result;


    public static final String YEARLY_CHECK_ID = "yearly_check_id";

    public static final String CAR_ID = "car_id";

    public static final String YEAR_CHECK_NEXT_DATE = "year_check_next_date";

    public static final String YEAR_CHECK_STATE = "year_check_state";

    public static final String RESULT = "result";

    @Override
    protected Serializable pkVal() {
        return this.yearlyCheckId;
    }

}
