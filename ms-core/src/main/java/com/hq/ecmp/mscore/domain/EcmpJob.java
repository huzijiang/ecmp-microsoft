package com.hq.ecmp.mscore.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 定时任务调度对象 ecmp_job
 *
 * @author hqer
 * @date 2020-01-02
 */
@Data
public class EcmpJob extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 任务ID */
    private Long jobId;

    /** 任务名称 */
    private String jobName;

    /** 任务组名 */
    private String jobGroup;

    /** 调用目标字符串 */
    @Excel(name = "调用目标字符串")
    private String invokeTarget;

    /** cron执行表达式 */
    @Excel(name = "cron执行表达式")
    private String cronExpression;

    /** 计划执行错误策略（1立即执行 2执行一次 3放弃执行） */
    @Excel(name = "计划执行错误策略", readConverterExp = "1=立即执行,2=执行一次,3=放弃执行")
    private String misfirePolicy;

    /** 是否并发执行（0允许 1禁止） */
    @Excel(name = "是否并发执行", readConverterExp = "0=允许,1=禁止")
    private String concurrent;

    /** 状态（0正常 1暂停） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=暂停")
    private String status;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("jobId", getJobId())
            .append("jobName", getJobName())
            .append("jobGroup", getJobGroup())
            .append("invokeTarget", getInvokeTarget())
            .append("cronExpression", getCronExpression())
            .append("misfirePolicy", getMisfirePolicy())
            .append("concurrent", getConcurrent())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
