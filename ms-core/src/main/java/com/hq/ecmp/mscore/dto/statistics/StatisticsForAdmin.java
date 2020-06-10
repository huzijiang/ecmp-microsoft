package com.hq.ecmp.mscore.dto.statistics;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class StatisticsForAdmin {

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
    //部门id
    @ApiModelProperty(value = "部门id")
    private Long deptId;
    //车牌号
    @ApiModelProperty(value = "车牌号")
    private String carLicense;
    //驾驶员id
    @ApiModelProperty(value = "驾驶员id")
    private String driverId;
    //车型名字
    @ApiModelProperty(value = "车型名字")
    private String carName;
    //车队名字
    @ApiModelProperty(value = "车队名字")
    private String carGroupName;

}
