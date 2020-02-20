package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 car_group_info
 *
 * @author hqer
 * @date 2020-01-02
 */
public class CarGroupInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long carGroupId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long city;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String cityName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long ownerOrg;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long leader;

    public void setCarGroupId(Long carGroupId)
    {
        this.carGroupId = carGroupId;
    }

    public Long getCarGroupId()
    {
        return carGroupId;
    }
    public void setCity(Long city)
    {
        this.city = city;
    }

    public Long getCity()
    {
        return city;
    }
    public void setCityName(String cityName)
    {
        this.cityName = cityName;
    }

    public String getCityName()
    {
        return cityName;
    }
    public void setOwnerOrg(Long ownerOrg)
    {
        this.ownerOrg = ownerOrg;
    }

    public Long getOwnerOrg()
    {
        return ownerOrg;
    }
    public void setLeader(Long leader)
    {
        this.leader = leader;
    }

    public Long getLeader()
    {
        return leader;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("carGroupId", getCarGroupId())
            .append("city", getCity())
            .append("cityName", getCityName())
            .append("ownerOrg", getOwnerOrg())
            .append("leader", getLeader())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
