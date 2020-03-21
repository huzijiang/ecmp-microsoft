package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 approve_template_node_info
 *
 * @author hqer
 * @date 2020-01-02
 */
public class ApproveTemplateNodeInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long approveNodeId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long approveTemplateId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String approverType;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String leaderLevel;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String roleId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String userId;
    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String nextNodeId;

    public void setApproveNodeId(Long approveNodeId)
    {
        this.approveNodeId = approveNodeId;
    }

    public Long getApproveNodeId()
    {
        return approveNodeId;
    }
    public void setApproveTemplateId(Long approveTemplateId)
    {
        this.approveTemplateId = approveTemplateId;
    }

    public Long getApproveTemplateId()
    {
        return approveTemplateId;
    }
    public void setApproverType(String approverType)
    {
        this.approverType = approverType;
    }

    public String getApproverType()
    {
        return approverType;
    }
    public void setLeaderLevel(String leaderLevel)
    {
        this.leaderLevel = leaderLevel;
    }

    public String getLeaderLevel()
    {
        return leaderLevel;
    }
    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
    }

    public String getRoleId()
    {
        return roleId;
    }
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getNextNodeId() {
        return nextNodeId;
    }

    public void setNextNodeId(String nextNodeId) {
        this.nextNodeId = nextNodeId;
    }

    public ApproveTemplateNodeInfo() {
    }
    public ApproveTemplateNodeInfo(Long approveTemplateId) {
        this.approveTemplateId = approveTemplateId;
    }
    public ApproveTemplateNodeInfo( String userId,boolean flag) {
        if (flag){
            this.userId = userId;
        }
    }
    public ApproveTemplateNodeInfo(Long approveTemplateId, String userId) {
        this.approveTemplateId = approveTemplateId;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("approveNodeId", getApproveNodeId())
            .append("approveTemplateId", getApproveTemplateId())
            .append("approverType", getApproverType())
            .append("leaderLevel", getLeaderLevel())
            .append("roleId", getRoleId())
            .append("userId", getUserId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
