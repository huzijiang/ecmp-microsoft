package com.hq.ecmp.mscore.domain;
/**update2**/

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.hq.ecmp.mscore.domain.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
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
public class CarGroupInfo extends BaseEntity<CarGroupInfo> {

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
