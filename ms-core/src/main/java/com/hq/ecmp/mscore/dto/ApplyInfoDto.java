package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 申请单VO
 * @author xueyong
 * @date 2020/1/3
 * ecmp-proxy.
 */
@Data
public class ApplyInfoDto {

    /**
     * 用车权限ID
     */
    private Integer ticketId;


    /**
     * 用车制度ID
     */
    private Integer regulationId;

    /**
     * 申请类型名称 eg:公务、差旅
     */
    private String applyTypeName;

    /**
     * 申请ID
     */
    private Integer applyId;

    /**
     * 申请原因|名称（如果是差旅 eg显示城市的顺序）
     */
    private String reason;

    /**
     * 状态
     */
    private Integer status;
    /**
     * 申请日期
     */
    private Date applyDate;

    /**
     * 申请类型 eg：公务、差旅
     */
    private Integer type;
}