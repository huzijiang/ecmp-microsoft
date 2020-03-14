package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 申请单VO
 * @author xueyong
 * @date 2020/1/3
 * ecmp-proxy.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplyInfoDTO {

    /**
     * 申请ID
     */
    private Long applyId;
    /**
     * 申请原因|名称（如果是差旅 eg显示城市的顺序）
     */
    private String title;   // TODO  reason改动
    /**
     * 状态
     */
    private String state; //TODO Integer status改动
    /**
     * 申请用车日期
     */
    private Date startDate; //TODO applyDate改動

    /**
     * 结束日期
     */
    private Date endDate;

    /**
     * 申请类型 eg：公务、差旅
     */
    private String applyType;   //TODO Integer type改动

}