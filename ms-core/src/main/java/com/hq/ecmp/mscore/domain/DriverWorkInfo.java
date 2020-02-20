package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;
import java.util.Date;

/**
 * 【请填写功能名称】对象 driver_work_info
 *
 * @author hqer
 * @date 2020-01-02
 */
public class DriverWorkInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long workId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long driverId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String leaveStatus;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String leaveConfirmStatus;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Date caledarDate;

    public void setWorkId(Long workId)
    {
        this.workId = workId;
    }

    public Long getWorkId()
    {
        return workId;
    }
    public void setDriverId(Long driverId)
    {
        this.driverId = driverId;
    }

    public Long getDriverId()
    {
        return driverId;
    }
    public void setLeaveStatus(String leaveStatus)
    {
        this.leaveStatus = leaveStatus;
    }

    public String getLeaveStatus()
    {
        return leaveStatus;
    }
    public void setLeaveConfirmStatus(String leaveConfirmStatus)
    {
        this.leaveConfirmStatus = leaveConfirmStatus;
    }

    public String getLeaveConfirmStatus()
    {
        return leaveConfirmStatus;
    }
    public void setCaledarDate(Date caledarDate)
    {
        this.caledarDate = caledarDate;
    }

    public Date getCaledarDate()
    {
        return caledarDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("workId", getWorkId())
            .append("driverId", getDriverId())
            .append("leaveStatus", getLeaveStatus())
            .append("leaveConfirmStatus", getLeaveConfirmStatus())
            .append("caledarDate", getCaledarDate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
