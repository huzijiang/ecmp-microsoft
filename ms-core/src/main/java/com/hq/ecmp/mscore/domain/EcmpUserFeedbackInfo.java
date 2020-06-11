package com.hq.ecmp.mscore.domain;

import com.sun.jna.platform.win32.WinDef;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.core.web.domain.BaseEntity;

import java.util.List;

/**
 * 【请填写功能名称】对象 ecmp_user_feedback_info
 *
 * @author hqer
 * @date 2020-01-02
 */
@Data
public class EcmpUserFeedbackInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long feedbackId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long userId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long orderId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String type;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String content;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String result;

    //单位id
    private Long ecmpId;
    //投诉标题
    private String title;
    //状态
    private int status;
    //起始页
    private int pageIndex;
    //显示条数
    private Integer pageSize;

    //显示条数
    private int isAdmin;


    public EcmpUserFeedbackInfo() {
    }

    public EcmpUserFeedbackInfo(Long orderId) {
        this.orderId = orderId;
    }
}
