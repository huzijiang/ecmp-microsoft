package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: chao.zhang
 * @Date: 2020/6/12 10:17
 */
@Data
public class UserDeptUseCarDetailDto {

    //开始时间
    @ApiModelProperty(value = "开始时间")
    private String beginDate;
    //结束时间
    @ApiModelProperty(value = "结束时间")
    private String endDate;
    //起始页码
    @ApiModelProperty(name = "pageNum", value = "起始页码",required = false)
    private Integer pageNum;
    //每页显示条数
    @ApiModelProperty(name = "pageSize", value = "每页显示条数",required = false)
    private Integer pageSize;
    @ApiModelProperty(value = "车队名字")
    private String carGroupName;
}
