package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName
 * @Description TODO 调度派车入参
 * @Author yj
 * @Date 2020/5/25 16:18
 * @Version 1.0
 */
@Data
@ApiModel(value = "派车接口入参")
public class DispatchSendCarDto {

    @ApiModelProperty(value = "订单id")
    private Long orderId;
    @ApiModelProperty(value = "派车车队类型，C000 内部  C111 外部")
    private String useCarGroupType;

    @ApiModelProperty(value = "自有车队使用模式 CA00  车和驾驶员都用\n" +
            "CA01  只用车\n" +
            "CA10  只用驾驶员\n" +
            "CA11  车和司机都不用")
    private String  carGroupUseMode;

    @ApiModelProperty(value = "是否自驾 Y000 是  N111 否")
    private String selfDrive;

    @ApiModelProperty(value = "内部调度所选的外部车队id")
    private  Long outCarGroupId;

    @ApiModelProperty(value = "调度车辆id")
    private Long  carId;

    @ApiModelProperty(value = "调度车辆所在车队id")
    private Long  carGroupId;

    @ApiModelProperty(value = "调度司机id")
    private Long driverId;

    @ApiModelProperty(value = "调度司机所选的司机所在车队id")
    private Long driverCarGroupId;

    @ApiModelProperty(value = "包车类型 T001 半日租（4小时）\n" +
            "T002 整日租（8小时）\n" +
            "T009 多日租 ( 多日租)")
    private String charterType;

    @ApiModelProperty(value = "用车车型id")
    private String carTypeId;
    @ApiModelProperty(value = "内部调度员操作或者是外部调度员操作 1 内部  2 外部")
    private int inOrOut;

    @ApiModelProperty(value = "是否完成调度 1 是 2 否",hidden = true)
    private int isFinishDispatch;

    @ApiModelProperty(value = "操作用id",hidden = true)
   private Long userId;

}
