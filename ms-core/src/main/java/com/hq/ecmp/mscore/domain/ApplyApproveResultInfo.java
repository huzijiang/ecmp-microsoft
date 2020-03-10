package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 apply_approve_result_info
 *
 * @author hqer
 * @date 2020-01-02
 */
public class ApplyApproveResultInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long approveResultId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long applyId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long approveTemplateId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long approveNodeId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String approver;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String approverMobile;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String approveResult;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String state;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String content;

    public void setApproveResultId(Long approveResultId)
    {
        this.approveResultId = approveResultId;
    }

    public Long getApproveResultId()
    {
        return approveResultId;
    }
    public void setApplyId(Long applyId)
    {
        this.applyId = applyId;
    }

    public Long getApplyId()
    {
        return applyId;
    }
    public void setApproveTemplateId(Long approveTemplateId)
    {
        this.approveTemplateId = approveTemplateId;
    }

    public Long getApproveTemplateId()
    {
        return approveTemplateId;
    }
    public void setApproveNodeId(Long approveNodeId)
    {
        this.approveNodeId = approveNodeId;
    }

    public Long getApproveNodeId()
    {
        return approveNodeId;
    }
    public void setApprover(String approver)
    {
        this.approver = approver;
    }

    public String getApprover()
    {
        return approver;
    }
    public void setApproverMobile(String approverMobile)
    {
        this.approverMobile = approverMobile;
    }

    public String getApproverMobile()
    {
        return approverMobile;
    }
    public void setApproveResult(String approveResult)
    {
        this.approveResult = approveResult;
    }

    public String getApproveResult()
    {
        return approveResult;
    }
    public void setState(String state)
    {
        this.state = state;
    }

    public String getState()
    {
        return state;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }

    public ApplyApproveResultInfo() {
    }

    public ApplyApproveResultInfo(Long applyId) {
        this.applyId = applyId;
    }

    public ApplyApproveResultInfo(Long applyId, Long approveTemplateId) {
        this.applyId = applyId;
        this.approveTemplateId = approveTemplateId;
    }

    public ApplyApproveResultInfo(Long applyId,Long approveTemplateId,Long approveNodeId) {
        this.applyId = applyId;
        this.approveNodeId=approveNodeId;
        this.approveTemplateId = approveTemplateId;
    }

    public ApplyApproveResultInfo(Long applyId, Long approveTemplateId, Long approveNodeId, String state) {
        this.applyId = applyId;
        this.approveTemplateId = approveTemplateId;
        this.approveNodeId = approveNodeId;
        this.state = state;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("approveResultId", getApproveResultId())
            .append("applyId", getApplyId())
            .append("approveTemplateId", getApproveTemplateId())
            .append("approveNodeId", getApproveNodeId())
            .append("approver", getApprover())
            .append("approverMobile", getApproverMobile())
            .append("approveResult", getApproveResult())
            .append("state", getState())
            .append("content", getContent())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
