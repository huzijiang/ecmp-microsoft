package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName OrderTraceDto
 * @Description TODO 和云端获取网约车轨迹接口交互获取到的数据格式
 * @Author yj
 * @Date 2020/3/19 18:39
 * @Version 1.0
 */
@Data
@ApiModel("网约车接口获取轨迹model")
public class OrderTraceDto {

    @ApiModelProperty("精度")
    private String x;
    @ApiModelProperty("纬度")
    private String  y;
    @ApiModelProperty("时间戳")
    private String pt;
}
