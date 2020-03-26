package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author
 * @date 2020/3/9
 *
 */
@Data
@ApiModel(description = "行程信息")
public class JourneyVO {
    @ApiModelProperty(name = "applyType",value = "用车申请类型")
    private String applyType;
    @ApiModelProperty(name = "carMode",value = "用车方式")
    private String carMode;
    @ApiModelProperty(name = "setOutTime",value = "出发时间")
    private String setOutTime;
    @ApiModelProperty(name = "arrivalAddress",value = "到达地址")
    private String arrivalAddress;
    @ApiModelProperty(name = "serviceType",value = "服务类型")
    private String serviceType;
    @ApiModelProperty(name = "itIsReturn",value = "是否往返")
    private String itIsReturn;
    @ApiModelProperty(name = "type",value = "去程/回程")
    private String type;


}
