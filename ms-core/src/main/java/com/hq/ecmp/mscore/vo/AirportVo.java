package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author caobj
 * @date 2020/3/3
 * ecmp-proxy.
 */
@Data
@ApiModel(description = "机场模型")
public class AirportVo {

    @ApiModelProperty(name = "airPortAddress",value = "机场详细地址")
    private String airPortAddress;
    @ApiModelProperty(name = "airPortLocation",value = "机场坐标经纬度，格式 : lng,lat")
    private String airPortLocation;
    @ApiModelProperty(name = "airPortName",value = "机场名称")
    private String airPortName;
}
