package com.hq.ecmp.mscore.domain;
/**update2**/

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.hq.ecmp.mscore.domain.base.MicBaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
@TableName("enterprise_car_type_info")
public class EnterpriseCarTypeInfo extends MicBaseEntity<EnterpriseCarTypeInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "car_type_id", type = IdType.AUTO)
    private Long carTypeId;

    private Long enterpriseId;

    private Long countryCarTypeId;

    private String name;

    private String level;

    @TableLogic
    private String status;


    public static final String CAR_TYPE_ID = "car_type_id";

    public static final String ENTERPRISE_ID = "enterprise_id";

    public static final String COUNTRY_CAR_TYPE_ID = "country_car_type_id";

    public static final String NAME = "name";

    public static final String LEVEL = "level";

    public static final String STATUS = "status";

    @Override
    protected Serializable pkVal() {
        return this.carTypeId;
    }

}
