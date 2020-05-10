package com.hq.ecmp.mscore.dto;

import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 通知公告对象 ecmp_notice
 *
 * @author hqer
 * @date 2020-01-02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EcmpNoticeDTO
{
    private static final long serialVersionUID = 1L;
    private Integer noticeId;

    /** 公告标题 */
    private String noticeTitle;

    /** 公告类型（1通知 2公告） */
    private String noticeType;

    /** 公告内容 */
    private String noticeContent;

    /** 公告首图 */
    private String noticeIcon;

    /** 公告状态（0正常 1关闭） */
    private String status;

    /** 公告推送时间 */
    private String publishTime;

    /** 结束时间 */
    private String endTime;

    /**
     * 类型：1.全部用户，2.角色，3.部门
     */
    private String configType;
    /**
     * 对应id
     */
    private Long bucId;
    /**
     * 城市id
     */
    private String noticeCity;
    /**
     * 多条对应id
     */
    private List<Long> bucIds;

}
