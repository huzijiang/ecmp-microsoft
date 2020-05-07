package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 cost_config_car_type_info
 * 
 * @author hqer
 * @date 2020-05-06
 */
public class CostConfigCarTypeInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String id;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long costId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long carTypeId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String carTypeName;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setCostId(Long costId) 
    {
        this.costId = costId;
    }

    public Long getCostId() 
    {
        return costId;
    }
    public void setCarTypeId(Long carTypeId) 
    {
        this.carTypeId = carTypeId;
    }

    public Long getCarTypeId() 
    {
        return carTypeId;
    }
    public void setCarTypeName(String carTypeName) 
    {
        this.carTypeName = carTypeName;
    }

    public String getCarTypeName() 
    {
        return carTypeName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("costId", getCostId())
            .append("carTypeId", getCarTypeId())
            .append("carTypeName", getCarTypeName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}