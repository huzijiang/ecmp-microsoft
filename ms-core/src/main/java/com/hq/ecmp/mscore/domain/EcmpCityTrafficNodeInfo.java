package com.hq.ecmp.mscore.domain;
/**update2**/

import java.math.BigDecimal;
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
@TableName("ecmp_city_traffic_node_info")
public class EcmpCityTrafficNodeInfo extends BaseEntity<EcmpCityTrafficNodeInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "traffic_node_id", type = IdType.AUTO)
    private Integer trafficNodeId;

    private Integer cityId;

    private String cityLevel;

    private String name;

    private String type;

    private BigDecimal longitude;

    private BigDecimal latitude;


    public static final String TRAFFIC_NODE_ID = "traffic_node_id";

    public static final String CITY_ID = "city_id";

    public static final String CITY_LEVEL = "city_level";

    public static final String NAME = "name";

    public static final String TYPE = "type";

    public static final String LONGITUDE = "longitude";

    public static final String LATITUDE = "latitude";

    @Override
    protected Serializable pkVal() {
        return this.trafficNodeId;
    }

}
