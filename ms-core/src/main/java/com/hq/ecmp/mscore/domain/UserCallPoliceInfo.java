package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 user_call_police_info
 *
 * @author hqer
 * @date 2020-01-02
 */
public class UserCallPoliceInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String userId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String userPhone;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String area;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long longitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long latitude;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String carLicense;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String journeyId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String state;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String result;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUserId()
    {
        return userId;
    }
    public void setUserPhone(String userPhone)
    {
        this.userPhone = userPhone;
    }

    public String getUserPhone()
    {
        return userPhone;
    }
    public void setArea(String area)
    {
        this.area = area;
    }

    public String getArea()
    {
        return area;
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
    public void setCarLicense(String carLicense)
    {
        this.carLicense = carLicense;
    }

    public String getCarLicense()
    {
        return carLicense;
    }
    public void setJourneyId(String journeyId)
    {
        this.journeyId = journeyId;
    }

    public String getJourneyId()
    {
        return journeyId;
    }
    public void setState(String state)
    {
        this.state = state;
    }

    public String getState()
    {
        return state;
    }
    public void setResult(String result)
    {
        this.result = result;
    }

    public String getResult()
    {
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("userPhone", getUserPhone())
            .append("area", getArea())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("carLicense", getCarLicense())
            .append("journeyId", getJourneyId())
            .append("state", getState())
            .append("result", getResult())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
