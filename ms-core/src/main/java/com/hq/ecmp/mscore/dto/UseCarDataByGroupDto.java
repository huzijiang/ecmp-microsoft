package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: chao.zhang
 * @Date: 2020/6/9 17:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UseCarDataByGroupDto {

    @ApiModelProperty(value = "开始时间")
    private Date beginDate;

    @ApiModelProperty(value = "结束时间")
    private Date endDate;

    @ApiModelProperty(value = "每页显示数量")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页码")
    private Integer pageNum;

    @ApiModelProperty(value = "车队id")
    private Long carGroupId;
}
