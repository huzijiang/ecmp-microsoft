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
@TableName("scene_regime_relation")
public class SceneRegimeRelation extends MicBaseEntity<SceneRegimeRelation> {

    private static final long serialVersionUID=1L;

    private Long sceneId;

    private Long regimenId;


    public static final String SCENE_ID = "scene_id";

    public static final String REGIMEN_ID = "regimen_id";

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
