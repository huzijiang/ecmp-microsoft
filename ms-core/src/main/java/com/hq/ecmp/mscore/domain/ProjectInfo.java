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
@TableName("project_info")
public class ProjectInfo extends MicBaseEntity<ProjectInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "project_id", type = IdType.AUTO)
    private Long projectId;

    private String name;

    private Integer leader;

    private String projectCode;

    private String startDate;

    private String closeDate;


    public static final String PROJECT_ID = "project_id";

    public static final String NAME = "name";

    public static final String LEADER = "leader";

    public static final String PROJECT_CODE = "project_code";

    public static final String START_DATE = "start_date";

    public static final String CLOSE_DATE = "close_date";

    @Override
    protected Serializable pkVal() {
        return this.projectId;
    }

}
