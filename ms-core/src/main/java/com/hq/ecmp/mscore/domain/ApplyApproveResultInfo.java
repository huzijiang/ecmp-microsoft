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
@TableName("apply_approve_result_info")
public class ApplyApproveResultInfo extends MicBaseEntity<ApplyApproveResultInfo> {

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
