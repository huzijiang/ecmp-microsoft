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
@TableName("user_call_police_info")
public class UserCallPoliceInfo extends MicBaseEntity<UserCallPoliceInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String userId;

    private String userPhone;

    private String area;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private String carLicense;

    private String journeyId;

    private String state;

    private String result;


    public static final String ID = "id";

    public static final String USER_ID = "user_id";

    public static final String USER_PHONE = "user_phone";

    public static final String AREA = "area";

    public static final String LONGITUDE = "longitude";

    public static final String LATITUDE = "latitude";

    public static final String CAR_LICENSE = "car_license";

    public static final String JOURNEY_ID = "journey_id";

    public static final String STATE = "state";

    public static final String RESULT = "result";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
