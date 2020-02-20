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
@TableName("approve_template_info")
public class ApproveTemplateInfo extends MicBaseEntity<ApproveTemplateInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "approve_template_id", type = IdType.AUTO)
    private Long approveTemplateId;

    private String name;


    public static final String APPROVE_TEMPLATE_ID = "approve_template_id";

    public static final String NAME = "name";

    @Override
    protected Serializable pkVal() {
        return this.approveTemplateId;
    }

}
