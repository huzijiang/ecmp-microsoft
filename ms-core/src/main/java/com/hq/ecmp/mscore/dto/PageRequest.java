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

    //搜索字段
    @ApiModelProperty(name = "state", value = "状态",required = false)
    private String state;
    @ApiModelProperty(name = "type", value = "注册用户类型：T001员工/T002驾驶员",required = false)
    private String type;

    @ApiModelProperty(name = "deptType", value = "组织类型",required = false)
    private String deptType;

    //搜索字段
    @ApiModelProperty(name = "level", value = "车型级别",required = false)
    private String level;

    //搜索字段
    @ApiModelProperty(name = "deptId", value = "组织id",required = false)
    private Long deptId;

    @ApiModelProperty(name = "carTypeId", value = "车型级别id",required = false)
    private Long carTypeId;

    @ApiModelProperty(name = "costId", value = "成本id",required = false)
    private Long costId;

    private Long carId;

    private Long driverId;

    private Long carGroupId;
    private String date;
    private Long fatherProjectId;

    private String workState;
    private String itIsFullTime;
    private String businessFlag;

    private Long companyId;

    @ApiModelProperty(value = "是否查询待确认订单  1 是 2 否")
    private Integer  isConfirmState;


}
