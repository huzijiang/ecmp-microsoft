package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 journey_passenger_info
 *
 * @author hqer
 * @date 2020-01-02
 */
public class JourneyPassengerInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long journeyPassengerId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long journeyId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String name;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String mobile;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String itIsPeer;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Integer peerNumber;

    public void setJourneyPassengerId(Long journeyPassengerId)
    {
        this.journeyPassengerId = journeyPassengerId;
    }

    public Integer getPeerNumber() {
        return peerNumber;
    }

    public void setPeerNumber(Integer peerNumber) {
        this.peerNumber = peerNumber;
    }

    public Long getJourneyPassengerId()
    {
        return journeyPassengerId;
    }
    public void setJourneyId(Long journeyId)
    {
        this.journeyId = journeyId;
    }

    public Long getJourneyId()
    {
        return journeyId;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }

    public String getMobile()
    {
        return mobile;
    }
    public void setItIsPeer(String itIsPeer)
    {
        this.itIsPeer = itIsPeer;
    }

    public String getItIsPeer()
    {
        return itIsPeer;
    }

    public JourneyPassengerInfo() {
    }

    public JourneyPassengerInfo(Long journeyId) {
        this.journeyId = journeyId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("journeyPassengerId", getJourneyPassengerId())
            .append("journeyId", getJourneyId())
            .append("name", getName())
            .append("mobile", getMobile())
            .append("itIsPeer", getItIsPeer())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
