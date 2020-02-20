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
@TableName("user_regime_relation_info")
public class UserRegimeRelationInfo extends MicBaseEntity<UserRegimeRelationInfo> {

    private static final long serialVersionUID=1L;

    private Long userId;

    private Long regimenId;


    public static final String USER_ID = "user_id";

    public static final String REGIMEN_ID = "regimen_id";

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
