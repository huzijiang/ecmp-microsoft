package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author:
 * @Date: 2020-03-04 15:26
 */
@Data
@ApiModel("驾驶员排班")
public class DriverWorkDTO {

    @ApiModelProperty(value = "驾驶员ID")
    private  String  driverId;

    @ApiModelProperty(value = "休假状态")
    private  String  leaveStatus;

    @ApiModelProperty(value = "请假是否批准")
    private  String  leaveConfirmStaus;

    @ApiModelProperty(value = "日历中的哪一天")
    private  String  caledarDate;



}
