package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 scene_regime_relation
 *
 * @author hqer
 * @date 2020-01-02
 */
public class SceneRegimeRelation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long sceneId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long regimenId;

    public void setSceneId(Long sceneId)
    {
        this.sceneId = sceneId;
    }

    public Long getSceneId()
    {
        return sceneId;
    }
    public void setRegimenId(Long regimenId)
    {
        this.regimenId = regimenId;
    }

    public Long getRegimenId()
    {
        return regimenId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("sceneId", getSceneId())
            .append("regimenId", getRegimenId())
            .toString();
    }
}
