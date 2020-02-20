package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 ecmp_city_traffic_node_info
 *
 * @author hqer
 * @date 2020-01-02
 */
public class EcmpCityTrafficNodeInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long trafficNodeId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long cityId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String cityLevel;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String name;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String type;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long longitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long latitude;

    public void setTrafficNodeId(Long trafficNodeId)
    {
        this.trafficNodeId = trafficNodeId;
    }

    public Long getTrafficNodeId()
    {
        return trafficNodeId;
    }
    public void setCityId(Long cityId)
    {
        this.cityId = cityId;
    }

    public Long getCityId()
    {
        return cityId;
    }
    public void setCityLevel(String cityLevel)
    {
        this.cityLevel = cityLevel;
    }

    public String getCityLevel()
    {
        return cityLevel;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setLongitude(Long longitude)
    {
        this.longitude = longitude;
    }

    public Long getLongitude()
    {
        return longitude;
    }
    public void setLatitude(Long latitude)
    {
        this.latitude = latitude;
    }

    public Long getLatitude()
    {
        return latitude;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("trafficNodeId", getTrafficNodeId())
            .append("cityId", getCityId())
            .append("cityLevel", getCityLevel())
            .append("name", getName())
            .append("type", getType())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
