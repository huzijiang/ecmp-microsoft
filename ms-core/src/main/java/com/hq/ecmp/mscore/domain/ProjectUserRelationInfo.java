package com.hq.ecmp.mscore.domain;
/**update2**/

import com.baomidou.mybatisplus.annotation.TableName;
import com.hq.ecmp.mscore.domain.base.MicBaseEntity;

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
@TableName("project_user_relation_info")
public class ProjectUserRelationInfo extends MicBaseEntity<ProjectUserRelationInfo> {

    private static final long serialVersionUID=1L;

    private Long projectId;

    private Long userId;


    public static final String PROJECT_ID = "project_id";

    public static final String USER_ID = "user_id";

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
