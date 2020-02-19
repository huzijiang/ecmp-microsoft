package com.hq.ecmp.mscore.domain;
/**update2**/

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.hq.ecmp.mscore.domain.base.BaseEntity;
import java.time.LocalDateTime;
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
@TableName("journey_node_info")
public class JourneyNodeInfo extends BaseEntity<JourneyNodeInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "node_id", type = IdType.AUTO)
    private Long nodeId;

    private Long journeyId;

    private Integer userId;

    private String planBeginAddress;

    private String planEndAddress;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime planSetoutTime;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime planArriveTime;

    private BigDecimal planBeginLongitude;

    private String planBeginLatitude;

    private BigDecimal planEndLongitude;

    private String planEndLatitude;

    private String itIsViaPoint;

    private String vehicle;

    private String duration;

    private String distance;

    private String waitDuration;

    private String nodeState;

    private Integer number;


    public static final String NODE_ID = "node_id";

    public static final String JOURNEY_ID = "journey_id";

    public static final String USER_ID = "user_id";

    public static final String PLAN_BEGIN_ADDRESS = "plan_begin_address";

    public static final String PLAN_END_ADDRESS = "plan_end_address";

    public static final String PLAN_SETOUT_TIME = "plan_setout_time";

    public static final String PLAN_ARRIVE_TIME = "plan_arrive_time";

    public static final String PLAN_BEGIN_LONGITUDE = "plan_begin_longitude";

    public static final String PLAN_BEGIN_LATITUDE = "plan_begin_latitude";

    public static final String PLAN_END_LONGITUDE = "plan_end_longitude";

    public static final String PLAN_END_LATITUDE = "plan_end_latitude";

    public static final String IT_IS_VIA_POINT = "it_is_via_point";

    public static final String VEHICLE = "vehicle";

    public static final String DURATION = "duration";

    public static final String DISTANCE = "distance";

    public static final String WAIT_DURATION = "wait_duration";

    public static final String NODE_STATE = "node_state";

    public static final String NUMBER = "number";

    @Override
    protected Serializable pkVal() {
        return this.nodeId;
    }

}
