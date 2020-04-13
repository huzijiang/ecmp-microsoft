package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
/**
 * @Author:
 * @Date: 2020-03-04 15:26
 */
@Data
@ApiModel("驾驶员失效列表")
public class DriverLoseDTO {
    @ApiModelProperty(value = "驾驶员id")
    private Long driverId;
    @ApiModelProperty(value = "工号")
    private Long userId;
    @ApiModelProperty(value = "驾驶员姓名")
    private String driverName;

    @ApiModelProperty(value = "电话号码")
    private String mobile;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "车队名称")
    private String carGroupName;

    @ApiModelProperty(value = "离职时间")
    private Date dimissionTime;

    @ApiModelProperty(value = "状态描述")
    private String stateDescription;





}
