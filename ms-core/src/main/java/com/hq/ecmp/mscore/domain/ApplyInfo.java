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
@TableName("apply_info")
public class ApplyInfo extends BaseEntity<ApplyInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "apply_id", type = IdType.AUTO)
    private Long applyId;

    private Long journeyId;

    private Long projectId;

    private Long regimenId;

    private String applyType;

    private String approverName;

    private Integer costCenter;

    private String state;

    private String reason;


    public static final String APPLY_ID = "apply_id";

    public static final String JOURNEY_ID = "journey_id";

    public static final String PROJECT_ID = "project_id";

    public static final String REGIMEN_ID = "regimen_id";

    public static final String APPLY_TYPE = "apply_type";

    public static final String APPROVER_NAME = "approver_name";

    public static final String COST_CENTER = "cost_center";

    public static final String STATE = "state";

    public static final String REASON = "reason";

    @Override
    protected Serializable pkVal() {
        return this.applyId;
    }

}
