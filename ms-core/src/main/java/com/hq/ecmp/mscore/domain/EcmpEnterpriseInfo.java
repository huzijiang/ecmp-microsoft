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
@TableName("ecmp_enterprise_info")
public class EcmpEnterpriseInfo extends MicBaseEntity<EcmpEnterpriseInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "enterprise_id", type = IdType.AUTO)
    private Integer enterpriseId;

    private Long deptId;

    private String name;

    private String address;

    private String mobile;

    private String uscc;


    public static final String ENTERPRISE_ID = "enterprise_id";

    public static final String DEPT_ID = "dept_id";

    public static final String NAME = "name";

    public static final String ADDRESS = "address";

    public static final String MOBILE = "mobile";

    public static final String USCC = "uscc";

    @Override
    protected Serializable pkVal() {
        return this.enterpriseId;
    }

}
