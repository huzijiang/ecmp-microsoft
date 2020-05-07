package com.hq.ecmp.mscore.domain;

import lombok.Data;
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
@Data
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
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String deptProjectId;
    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String nextNodeId;


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

    public ApproveTemplateNodeInfo(Long approveNodeId, String nextNodeId) {
        this.approveNodeId = approveNodeId;
        this.nextNodeId = nextNodeId;
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
