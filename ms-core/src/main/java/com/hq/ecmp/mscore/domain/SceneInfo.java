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
@TableName("scene_info")
public class SceneInfo extends BaseEntity<SceneInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "scene_id", type = IdType.AUTO)
    private Long sceneId;

    private String name;

    private Integer sortNo;

    private String icon;

    private String effectStatus;


    public static final String SCENE_ID = "scene_id";

    public static final String NAME = "name";

    public static final String SORT_NO = "sort_no";

    public static final String ICON = "icon";

    public static final String EFFECT_STATUS = "effect_status";

    @Override
    protected Serializable pkVal() {
        return this.sceneId;
    }

}
