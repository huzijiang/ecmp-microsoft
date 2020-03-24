package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author caobj
 * @date 2020/3/9
 *
 */
@Data
@ApiModel(description = "行程信息")
public class JourneyDetailVO {
    @ApiModelProperty(name = "applyType",value = "公务/差旅")
    private String applyType;
    @ApiModelProperty(name = "startAddress",value = "出发地")
    private String startAddress;
    @ApiModelProperty(name = "endAddress",value = "目的地")
    private String endAddress;
    @ApiModelProperty(name = "serviceType",value = "服务类型")
    private String serviceType;
    @ApiModelProperty(name = "charterCarType",value = "包车类型")
    private String charterCarType;
    @ApiModelProperty(name = "useCarMode",value = "用车方式")
    private String useCarMode;
    @ApiModelProperty(name = "powerId",value = "权限id")
    private Long powerId;
    @ApiModelProperty(name = "useCarTime",value = "用车时间")
    private String useCarTime;
    @ApiModelProperty(name = "timestamp",value = "时间错")
    private String timestamp;
    @ApiModelProperty(name = "endLatitude",value = "下车地纬度")
    private String endLatitude;//纬度
    @ApiModelProperty(name = "endLongitude",value = "下车地经度")
    private String endLongitude;//经度
    @ApiModelProperty(name = "startLatitude",value = "上车地纬度")
    private String startLatitude;//纬度
    @ApiModelProperty(name = "startLongitude",value = "上车地经度")
    private String startLongitude;//经度
    @ApiModelProperty(name = "startCityCode",value = "出发地城市code")
    private String startCityCode;//纬度
    @ApiModelProperty(name = "endCityCode",value = "目的地城市code")
    private String endCityCode;//经度

}
