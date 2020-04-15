package com.hq.ecmp.mscore.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 定时任务调度日志对象 ecmp_job_log
 *
 * @author hqer
 * @date 2020-01-02
 */
public class EcmpJobLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 任务日志ID */
    private Long jobLogId;

    /** 任务名称 */
    @Excel(name = "任务名称")
    private String jobName;

    /** 任务组名 */
    @Excel(name = "任务组名")
    private String jobGroup;

    /** 调用目标字符串 */
    @Excel(name = "调用目标字符串")
    private String invokeTarget;

    /** 日志信息 */
    @Excel(name = "日志信息")
    private String jobMessage;

    /** 执行状态（0正常 1失败） */
    @Excel(name = "执行状态", readConverterExp = "0=正常,1=失败")
    private String status;

    /** 异常信息 */
    @Excel(name = "异常信息")
    private String exceptionInfo;

    public void setJobLogId(Long jobLogId)
    {
        this.jobLogId = jobLogId;
    }

    public Long getJobLogId()
    {
        return jobLogId;
    }
    public void setJobName(String jobName)
    {
        this.jobName = jobName;
    }

    public String getJobName()
    {
        return jobName;
    }
    public void setJobGroup(String jobGroup)
    {
        this.jobGroup = jobGroup;
    }

    public String getJobGroup()
    {
        return jobGroup;
    }
    public void setInvokeTarget(String invokeTarget)
    {
        this.invokeTarget = invokeTarget;
    }

    public String getInvokeTarget()
    {
        return invokeTarget;
    }
    public void setJobMessage(String jobMessage)
    {
        this.jobMessage = jobMessage;
    }

    public String getJobMessage()
    {
        return jobMessage;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setExceptionInfo(String exceptionInfo)
    {
        this.exceptionInfo = exceptionInfo;
    }

    public String getExceptionInfo()
    {
        return exceptionInfo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("jobLogId", getJobLogId())
            .append("jobName", getJobName())
            .append("jobGroup", getJobGroup())
            .append("invokeTarget", getInvokeTarget())
            .append("jobMessage", getJobMessage())
            .append("status", getStatus())
            .append("exceptionInfo", getExceptionInfo())
            .append("createTime", getCreateTime())
            .toString();
    }
}
