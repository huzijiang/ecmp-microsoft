package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 角色和部门关联对象 ecmp_role_dept
 *
 * @author hqer
 * @date 2020-01-02
 */
public class EcmpRoleDept extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 角色ID */
    private Long roleId;

    /** 部门ID */
    private Long deptId;

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public Long getRoleId()
    {
        return roleId;
    }
    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public Long getDeptId()
    {
        return deptId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roleId", getRoleId())
            .append("deptId", getDeptId())
            .toString();
    }
}
