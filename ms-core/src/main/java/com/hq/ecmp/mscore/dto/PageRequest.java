package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/3 12:23
 */
@Data
public class PageRequest {

    //起始页码
    @ApiModelProperty(name = "pageNum", value = "起始页码",required = false)
    private Integer pageNum;
    //每页显示条数
    @ApiModelProperty(name = "pageSize", value = "每页显示条数",required = false)
    private Integer pageSize;
    //0:当天,1:明天,2:后天 3:大后天,-1:全部

    private Integer day;
    //搜索字段
    @ApiModelProperty(name = "search", value = "搜索关键字",required = false)
    private String search;

    private Long carId;

    private Long carGroupId;

    private Long fatherProjectId;
}
