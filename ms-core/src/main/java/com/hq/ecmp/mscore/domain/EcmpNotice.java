package com.hq.ecmp.mscore.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

/**
 * 通知公告对象 ecmp_notice
 *
 * @author hqer
 * @date 2020-01-02
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EcmpNotice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 公告ID */
    private Integer noticeId;

    /** 公告标题 */
    @Excel(name = "公告标题")
    private String noticeTitle;

    /** 公告类型（1通知 2公告） */
    @Excel(name = "公告类型", readConverterExp = "1=通知,2=公告")
    private String noticeType;

    /** 公告内容 */
    @Excel(name = "公告内容")
    private String noticeContent;

    /** 公告状态（0正常 1关闭） */
    @Excel(name = "公告状态", readConverterExp = "0=正常,1=关闭")
    private String status;

    /** 公告推送时间 */
    @Excel(name = "公告推送时间")
    private String publishTime;

    /** 结束时间 */
    @Excel(name = "结束时间")
    private String endTime;

    /** 发布对象*/
    @Excel(name = "结束时间")
    private String configType;


    public void setNoticeId(Integer noticeId)
    {
        this.noticeId = noticeId;
    }

    public Integer getNoticeId()
    {
        return noticeId;
    }
    public void setNoticeTitle(String noticeTitle)
    {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeTitle()
    {
        return noticeTitle;
    }
    public void setNoticeType(String noticeType)
    {
        this.noticeType = noticeType;
    }

    public String getNoticeType()
    {
        return noticeType;
    }
    public void setNoticeContent(String noticeContent)
    {
        this.noticeContent = noticeContent;
    }

    public String getNoticeContent()
    {
        return noticeContent;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("noticeId", getNoticeId())
            .append("noticeTitle", getNoticeTitle())
            .append("noticeType", getNoticeType())
            .append("noticeContent", getNoticeContent())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("configType", getConfigType())
            .toString();
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    @Override
    public String getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }
}
