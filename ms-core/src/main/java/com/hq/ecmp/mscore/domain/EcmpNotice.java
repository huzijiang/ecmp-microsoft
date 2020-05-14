package com.hq.ecmp.mscore.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

import java.util.List;

/**
 * 通知公告对象 ecmp_notice
 *
 * @author hqer
 * @date 2020-01-02
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EcmpNotice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 公告ID */
    private Integer noticeId;

    /**
     * 所属公司
     */
    private Long companyId;

    /** 公告标题 */
    @Excel(name = "公告标题")
    private String noticeTitle;

    /** 公告首图 */
    @Excel(name = "公告首图")
    private String noticeIcon;

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

    /**多条对应的公告id*/
    @Excel(name = "多条对应的公告id")
    private List<Long> bucIds;

    /** 发布城市*/
    @Excel(name = "发布城市")
    private String noticeCity;

    private List<String> noticeCode;
    private String bucId;
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
            .append("bucIds", getBucIds())
            .append("noticeCity", getNoticeCity())
            .append("noticeCode", getNoticeCode())
            .append("bucId", getBucId())
            .toString();
    }
}
