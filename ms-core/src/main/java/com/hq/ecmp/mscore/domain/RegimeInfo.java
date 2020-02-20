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
@TableName("regime_info")
public class RegimeInfo extends MicBaseEntity<RegimeInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "regimen_id", type = IdType.AUTO)
    private Long regimenId;

    private Long templateId;

    private Long approveTemplateId;

    private String applyType;

    private String name;

    private String allowCity;

    private String allowTime;

    private String setoutAddress;

    private String arriveAddress;

    private String allowDate;

    private String setoutEqualArrive;

    private String serviceType;

    private Integer approvalProcess;

    private Integer projectNeed;

    private String canUseCarMode;

    private String canUseCarLevel;

    private String remind;

    private Integer allowDateRoundTravel;

    private String allowCityRoundTravel;


    public static final String REGIMEN_ID = "regimen_id";

    public static final String TEMPLATE_ID = "template_id";

    public static final String APPROVE_TEMPLATE_ID = "approve_template_id";

    public static final String APPLY_TYPE = "apply_type";

    public static final String NAME = "name";

    public static final String ALLOW_CITY = "allow_city";

    public static final String ALLOW_TIME = "allow_time";

    public static final String SETOUT_ADDRESS = "setout_address";

    public static final String ARRIVE_ADDRESS = "arrive_address";

    public static final String ALLOW_DATE = "allow_date";

    public static final String SETOUT_EQUAL_ARRIVE = "setout_equal_arrive";

    public static final String SERVICE_TYPE = "service_type";

    public static final String APPROVAL_PROCESS = "approval_process";

    public static final String PROJECT_NEED = "project_need";

    public static final String CAN_USE_CAR_MODE = "can_use_car_mode";

    public static final String CAN_USE_CAR_LEVEL = "can_use_car_level";

    public static final String REMIND = "remind";

    public static final String ALLOW_DATE_ROUND_TRAVEL = "allow_date_round_travel";

    public static final String ALLOW_CITY_ROUND_TRAVEL = "allow_city_round_travel";

    @Override
    protected Serializable pkVal() {
        return this.regimenId;
    }

}
