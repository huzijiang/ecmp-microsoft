package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author:
 * @Date: 2020-03-04 15:26
 */
@Data
@ApiModel("驾驶员可用车辆模型")
public class DriverCanUseCarsDTO {

    @ApiModelProperty(value = "品牌")
    private  String  carType;

    @ApiModelProperty(value = "车牌号")
    private  String  carLicense;

    @ApiModelProperty(value = "能源类型")
    private  String  powerType;

    @ApiModelProperty(value = "资产编号")
    private  String  assetTag;

    @ApiModelProperty(value = "所属公司")
    private  String  deptName;

    @ApiModelProperty(value = "所属车队")
    private  String  carGroup;

    @ApiModelProperty(value = "车辆性质")
    private  String  source;

    @ApiModelProperty(value = "状态")
    private  String  state;



}
