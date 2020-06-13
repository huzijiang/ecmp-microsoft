package com.hq.ecmp.ms.api.dto.base;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 *
 * 意见反馈
 * @Author: zj.hu
 * @Date: 2020-01-06 15:40
 */
@Data
public class FeedBackDto {

    /**
     * 司机编号
     */
    @ApiParam(required = true)
    @NotEmpty
    private Long userId;


    /**
     * 反馈内容
     */
    private String content;

    private String orderId;


    /** 投诉id */
    private Long feedId;
    /** 用车单位 */
    private Long deptId;

    /** 用车状态 */
    private String status;

    /** 标题或内容 */
    private String title;

    /** 回复内容 */
    private String resultContent;

    //起始页
    private int pageNum;
    //显示条数
    private int pagesize;

}
