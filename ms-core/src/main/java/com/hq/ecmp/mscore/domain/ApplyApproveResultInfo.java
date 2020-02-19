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
@TableName("apply_approve_result_info")
public class ApplyApproveResultInfo extends BaseEntity<ApplyApproveResultInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "approve_result_id", type = IdType.AUTO)
    private Long approveResultId;

    private Long applyId;

    private Long approveTemplateId;

    private Long approveNodeId;

    private String approver;

    private String approverMobile;

    private String approveResult;

    private String state;

    private String content;


    public static final String APPROVE_RESULT_ID = "approve_result_id";

    public static final String APPLY_ID = "apply_id";

    public static final String APPROVE_TEMPLATE_ID = "approve_template_id";

    public static final String APPROVE_NODE_ID = "approve_node_id";

    public static final String APPROVER = "approver";

    public static final String APPROVER_MOBILE = "approver_mobile";

    public static final String APPROVE_RESULT = "approve_result";

    public static final String STATE = "state";

    public static final String CONTENT = "content";

    @Override
    protected Serializable pkVal() {
        return this.approveResultId;
    }

}
