package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 project_info
 *
 * @author hqer
 * @date 2020-01-02
 */
public class ProjectInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long projectId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String name;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String leader;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String projectCode;
    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long fatherProjectId;
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Integer isAllUserUse;
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Integer isEffective;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String startDate;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String closeDate;

    public void setProjectId(Long projectId)
    {
        this.projectId = projectId;
    }

    public Long getProjectId()
    {
        return projectId;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setLeader(String leader)
    {
        this.leader = leader;
    }

    public String getLeader()
    {
        return leader;
    }
    public void setProjectCode(String projectCode)
    {
        this.projectCode = projectCode;
    }

    public String getProjectCode()
    {
        return projectCode;
    }
    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }

    public String getStartDate()
    {
        return startDate;
    }
    public void setCloseDate(String closeDate)
    {
        this.closeDate = closeDate;
    }

    public String getCloseDate()
    {
        return closeDate;
    }

    public Long getFatherProjectId() {
        return fatherProjectId;
    }

    public void setFatherProjectId(Long fatherProjectId) {
        this.fatherProjectId = fatherProjectId;
    }

    public Integer getIsAllUserUse() {
        return isAllUserUse;
    }

    public void setIsAllUserUse(Integer isAllUserUse) {
        this.isAllUserUse = isAllUserUse;
    }

    public ProjectInfo() {
    }

    public Integer getIsEffective() {
        return isEffective;
    }

    public void setIsEffective(Integer isEffective) {
        this.isEffective = isEffective;
    }

    public ProjectInfo(String projectCode) {
        this.projectCode = projectCode;
    }

    public ProjectInfo(Long projectId, Integer isEffective) {
        this.projectId = projectId;
        this.isEffective = isEffective;
    }

    public ProjectInfo(Integer isEffective) {
        this.isEffective = isEffective;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("projectId", getProjectId())
            .append("name", getName())
            .append("leader", getLeader())
            .append("projectCode", getProjectCode())
            .append("startDate", getStartDate())
            .append("closeDate", getCloseDate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
