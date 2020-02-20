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
@TableName("approve_template_node_info")
public class ApproveTemplateNodeInfo extends MicBaseEntity<ApproveTemplateNodeInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "approve_node_id", type = IdType.AUTO)
    private Long approveNodeId;

    private Long approveTemplateId;

    private String approverType;

    private String leaderLevel;

    private String roleId;

    private Integer userId;


    public static final String APPROVE_NODE_ID = "approve_node_id";

    public static final String APPROVE_TEMPLATE_ID = "approve_template_id";

    public static final String APPROVER_TYPE = "approver_type";

    public static final String LEADER_LEVEL = "leader_level";

    public static final String ROLE_ID = "role_id";

    public static final String USER_ID = "user_id";

    @Override
    protected Serializable pkVal() {
        return this.approveNodeId;
    }

}
