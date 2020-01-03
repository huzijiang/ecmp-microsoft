package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 regime_template_info
 *
 * @author hqer
 * @date 2020-01-02
 */
public class RegimeTemplateInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long templateId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String name;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String allowCity;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String allowTime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String setoutAddress;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String arriveAddress;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String allowDate;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String setoutEqualArrive;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String serviceType;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long approvalProcess;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long projectNeed;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long canUseCarMode;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String canUseCarLevel;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String remind;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long allowDateRoundTravel;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String allowCityRoundTravel;

    public void setTemplateId(Long templateId)
    {
        this.templateId = templateId;
    }

    public Long getTemplateId()
    {
        return templateId;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setAllowCity(String allowCity)
    {
        this.allowCity = allowCity;
    }

    public String getAllowCity()
    {
        return allowCity;
    }
    public void setAllowTime(String allowTime)
    {
        this.allowTime = allowTime;
    }

    public String getAllowTime()
    {
        return allowTime;
    }
    public void setSetoutAddress(String setoutAddress)
    {
        this.setoutAddress = setoutAddress;
    }

    public String getSetoutAddress()
    {
        return setoutAddress;
    }
    public void setArriveAddress(String arriveAddress)
    {
        this.arriveAddress = arriveAddress;
    }

    public String getArriveAddress()
    {
        return arriveAddress;
    }
    public void setAllowDate(String allowDate)
    {
        this.allowDate = allowDate;
    }

    public String getAllowDate()
    {
        return allowDate;
    }
    public void setSetoutEqualArrive(String setoutEqualArrive)
    {
        this.setoutEqualArrive = setoutEqualArrive;
    }

    public String getSetoutEqualArrive()
    {
        return setoutEqualArrive;
    }
    public void setServiceType(String serviceType)
    {
        this.serviceType = serviceType;
    }

    public String getServiceType()
    {
        return serviceType;
    }
    public void setApprovalProcess(Long approvalProcess)
    {
        this.approvalProcess = approvalProcess;
    }

    public Long getApprovalProcess()
    {
        return approvalProcess;
    }
    public void setProjectNeed(Long projectNeed)
    {
        this.projectNeed = projectNeed;
    }

    public Long getProjectNeed()
    {
        return projectNeed;
    }
    public void setCanUseCarMode(Long canUseCarMode)
    {
        this.canUseCarMode = canUseCarMode;
    }

    public Long getCanUseCarMode()
    {
        return canUseCarMode;
    }
    public void setCanUseCarLevel(String canUseCarLevel)
    {
        this.canUseCarLevel = canUseCarLevel;
    }

    public String getCanUseCarLevel()
    {
        return canUseCarLevel;
    }
    public void setRemind(String remind)
    {
        this.remind = remind;
    }

    public String getRemind()
    {
        return remind;
    }
    public void setAllowDateRoundTravel(Long allowDateRoundTravel)
    {
        this.allowDateRoundTravel = allowDateRoundTravel;
    }

    public Long getAllowDateRoundTravel()
    {
        return allowDateRoundTravel;
    }
    public void setAllowCityRoundTravel(String allowCityRoundTravel)
    {
        this.allowCityRoundTravel = allowCityRoundTravel;
    }

    public String getAllowCityRoundTravel()
    {
        return allowCityRoundTravel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("templateId", getTemplateId())
            .append("name", getName())
            .append("allowCity", getAllowCity())
            .append("allowTime", getAllowTime())
            .append("setoutAddress", getSetoutAddress())
            .append("arriveAddress", getArriveAddress())
            .append("allowDate", getAllowDate())
            .append("setoutEqualArrive", getSetoutEqualArrive())
            .append("serviceType", getServiceType())
            .append("approvalProcess", getApprovalProcess())
            .append("projectNeed", getProjectNeed())
            .append("canUseCarMode", getCanUseCarMode())
            .append("canUseCarLevel", getCanUseCarLevel())
            .append("remind", getRemind())
            .append("allowDateRoundTravel", getAllowDateRoundTravel())
            .append("allowCityRoundTravel", getAllowCityRoundTravel())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
