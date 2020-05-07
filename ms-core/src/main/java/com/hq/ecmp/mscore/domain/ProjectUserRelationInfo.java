package com.hq.ecmp.mscore.domain;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 project_user_relation_info
 *
 * @author hqer
 * @date 2020-01-02
 */
@Data
public class ProjectUserRelationInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long projectId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long userId;


    public ProjectUserRelationInfo() {
    }

    public ProjectUserRelationInfo(Long projectId, Long userId) {
        this.projectId = projectId;
        this.userId = userId;
    }

    public ProjectUserRelationInfo(Long projectId) {
        this.projectId = projectId;
    }
    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;//地址相等
        }

        if(obj == null){
            return false;//非空性：对于任意非空引用x，x.equals(null)应该返回false。
        }

        if(obj instanceof ProjectUserRelationInfo){
            ProjectUserRelationInfo other = (ProjectUserRelationInfo) obj;
            //需要比较的字段相等，则这两个对象相等
            if(equalsStr(this.projectId, other.projectId)
                    && equalsStr(this.userId, other.userId)){
                return true;
            }
        }
        return false;
    }

    private boolean equalsStr(Long str1, Long str2){
        if(str1==str2){
            return true;
        }
        return false;
    }
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (projectId == null ? 0 : projectId.hashCode());
        result = 31 * result + (userId == null ? 0 : userId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("projectId", getProjectId())
            .append("userId", getUserId())
            .toString();
    }
}
