package com.hq.ecmp.mscore.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

import java.util.List;

/**
 * 部门对象 ecmp_org
 *
 * @author hqer
 * @date 2020-01-02
 */
@Data
public class EcmpOrg extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 组织id */
    private Long deptId;

    /** 上级组织id */
    @Excel(name = "上级组织id")
    private Long parentId;

    /** 所属公司ID */
    @Excel(name = "所属公司ID")
    private Long companyId;

    /** 企业自定义机构编码 */
    @Excel(name = "企业自定义机构编码")
    private String  deptCode;

    /** 祖级列表 */
    @Excel(name = "祖级列表")
    private String ancestors;

    /** 组织名称 */
    @Excel(name = "组织名称")
    private String deptName;

    /** 组织类别（1 公司 2 部门 3 车队） */
    @Excel(name = "组织类别", readConverterExp = "1=,公=司,2=,部=门,3=,车=队")
    private String deptType;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Integer orderNum;

    /** 负责人 */
    @Excel(name = "负责人")
    private String leader;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 部门状态（0正常 1停用） */
    @Excel(name = "部门状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /*新增临时字段 */
    private List<EcmpOrg> deptList;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("deptId", getDeptId())
            .append("parentId", getParentId())
            .append("companyId", getCompanyId())
            .append("ancestors", getAncestors())
            .append("deptName", getDeptName())
            .append("deptType", getDeptType())
            .append("orderNum", getOrderNum())
            .append("leader", getLeader())
            .append("phone", getPhone())
            .append("email", getEmail())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
