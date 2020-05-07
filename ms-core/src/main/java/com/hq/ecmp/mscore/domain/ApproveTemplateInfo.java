package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 approve_template_info
 *
 * @author hqer
 * @date 2020-01-02
 */
public class ApproveTemplateInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long approveTemplateId;

    /**
     * 审批流程模块所属公司ID
     */
    private Long ownerCompany;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String name;

    public void setApproveTemplateId(Long approveTemplateId)
    {
        this.approveTemplateId = approveTemplateId;
    }

    public Long getApproveTemplateId()
    {
        return approveTemplateId;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public ApproveTemplateInfo(String name) {
        this.name = name;
    }

    public ApproveTemplateInfo() {
    }

    public Long getOwnerCompany() {
        return ownerCompany;
    }

    public void setOwnerCompany(Long ownerCompany) {
        this.ownerCompany = ownerCompany;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("approveTemplateId", getApproveTemplateId())
            .append("name", getName())
            .append("ownerCompany", getOwnerCompany())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
