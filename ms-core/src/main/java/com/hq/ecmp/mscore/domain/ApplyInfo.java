package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 apply_info
 *
 * @author hqer
 * @date 2020-01-02
 */
public class ApplyInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long applyId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long journeyId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long projectId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long regimenId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String approverName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long costCenter;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String state;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String reason;

    public void setApplyId(Long applyId)
    {
        this.applyId = applyId;
    }

    public Long getApplyId()
    {
        return applyId;
    }
    public void setJourneyId(Long journeyId)
    {
        this.journeyId = journeyId;
    }

    public Long getJourneyId()
    {
        return journeyId;
    }
    public void setProjectId(Long projectId)
    {
        this.projectId = projectId;
    }

    public Long getProjectId()
    {
        return projectId;
    }
    public void setRegimenId(Long regimenId)
    {
        this.regimenId = regimenId;
    }

    public Long getRegimenId()
    {
        return regimenId;
    }
    public void setApproverName(String approverName)
    {
        this.approverName = approverName;
    }

    public String getApproverName()
    {
        return approverName;
    }
    public void setCostCenter(Long costCenter)
    {
        this.costCenter = costCenter;
    }

    public Long getCostCenter()
    {
        return costCenter;
    }
    public void setState(String state)
    {
        this.state = state;
    }

    public String getState()
    {
        return state;
    }
    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getReason()
    {
        return reason;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("applyId", getApplyId())
            .append("journeyId", getJourneyId())
            .append("projectId", getProjectId())
            .append("regimenId", getRegimenId())
            .append("approverName", getApproverName())
            .append("costCenter", getCostCenter())
            .append("state", getState())
            .append("reason", getReason())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
