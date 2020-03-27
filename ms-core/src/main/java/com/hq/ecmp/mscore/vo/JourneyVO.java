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
@ApiModel(description = "行程信息模型")
public class JourneyVO {
    @ApiModelProperty(name = "applyType",value = "用车申请类型")
    private String applyType;
    @ApiModelProperty(name = "carMode",value = "用车方式")
    private String carMode;
    @ApiModelProperty(name = "address",value = "到达地址")
    private String address;
    @ApiModelProperty(name = "actionTime",value = "出发时间")
    private String actionTime;
    @ApiModelProperty(name = "serviceType",value = "服务类型")
    private String serviceType;
    @ApiModelProperty(name = "type",value = "权限类别")
    private String type;


}
