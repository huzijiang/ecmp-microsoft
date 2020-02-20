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
@TableName("car_group_info")
public class CarGroupInfo extends MicBaseEntity<CarGroupInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "car_group_id", type = IdType.AUTO)
    private Long carGroupId;

    private Integer city;

    private String cityName;

    private Integer ownerOrg;

    private Integer leader;


    public static final String CAR_GROUP_ID = "car_group_id";

    public static final String CITY = "city";

    public static final String CITY_NAME = "city_name";

    public static final String OWNER_ORG = "owner_org";

    public static final String LEADER = "leader";

    @Override
    protected Serializable pkVal() {
        return this.carGroupId;
    }

}
