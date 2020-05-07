package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;
import java.util.Date;

/**
 * 【请填写功能名称】对象 cloud_work_date_info
 * 
 * @author hqer
 * @date 2020-05-07
 */
public class CloudWorkDateInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long companyId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date calendarDate;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String calendarYear;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String calendarMonth;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String calendarDay;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String itIsWork;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String festivalName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String workStart;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String workEnd;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCompanyId(Long companyId) 
    {
        this.companyId = companyId;
    }

    public Long getCompanyId() 
    {
        return companyId;
    }
    public void setCalendarDate(Date calendarDate) 
    {
        this.calendarDate = calendarDate;
    }

    public Date getCalendarDate() 
    {
        return calendarDate;
    }
    public void setCalendarYear(String calendarYear) 
    {
        this.calendarYear = calendarYear;
    }

    public String getCalendarYear() 
    {
        return calendarYear;
    }
    public void setCalendarMonth(String calendarMonth) 
    {
        this.calendarMonth = calendarMonth;
    }

    public String getCalendarMonth() 
    {
        return calendarMonth;
    }
    public void setCalendarDay(String calendarDay) 
    {
        this.calendarDay = calendarDay;
    }

    public String getCalendarDay() 
    {
        return calendarDay;
    }
    public void setItIsWork(String itIsWork) 
    {
        this.itIsWork = itIsWork;
    }

    public String getItIsWork() 
    {
        return itIsWork;
    }
    public void setFestivalName(String festivalName) 
    {
        this.festivalName = festivalName;
    }

    public String getFestivalName() 
    {
        return festivalName;
    }
    public void setWorkStart(String workStart) 
    {
        this.workStart = workStart;
    }

    public String getWorkStart() 
    {
        return workStart;
    }
    public void setWorkEnd(String workEnd) 
    {
        this.workEnd = workEnd;
    }

    public String getWorkEnd() 
    {
        return workEnd;
    }

    public CloudWorkDateInfo() {
    }

    public CloudWorkDateInfo(Date calendarDate) {
        this.calendarDate = calendarDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("companyId", getCompanyId())
            .append("calendarDate", getCalendarDate())
            .append("calendarYear", getCalendarYear())
            .append("calendarMonth", getCalendarMonth())
            .append("calendarDay", getCalendarDay())
            .append("itIsWork", getItIsWork())
            .append("festivalName", getFestivalName())
            .append("workStart", getWorkStart())
            .append("workEnd", getWorkEnd())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}