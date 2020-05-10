package com.hq.ecmp.mscore.domain;

import lombok.Data;
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
@Data
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
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long ownerCompany;
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long ownerOrg;


    public ProjectInfo() {
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
