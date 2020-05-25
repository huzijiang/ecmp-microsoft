package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xueyong
 * @date 2020/1/4
 * ecmp-proxy.
 */
@Data
@ApiModel(description = "价格总览")
public class PriceOverviewVO {
    @ApiModelProperty(name = "cityName",value = "城市名称")
    private String cityName;
    @ApiModelProperty(name = "cityCode",value = "城市代码")
    private String cityCode;  //TODO 新增
    @ApiModelProperty(name = "serviceType",value = "服务类型")
    private String serviceType;
    @ApiModelProperty(name = "carGroupUserMode",value = "服务模式")
    private String carGroupUserMode;
    @ApiModelProperty(name = "carGroupUserModeStr",value = "服务模式")
    private String carGroupUserModeStr;
    @ApiModelProperty(name = "rentType",value = "包车类型")
    private String rentType;
    @ApiModelProperty(name = "rentTypeStr",value = "包车类型")
    private String rentTypeStr;
    @ApiModelProperty(name = "carTypeId",value = "车型")
    private String carTypeId;
    @ApiModelProperty(name = "carTypeName",value = "车型")
    private String carTypeName;
    private List<CarGroupCostVO> costList;

    public PriceOverviewVO() {
    }

    public PriceOverviewVO(String rentType, List<CarGroupCostVO> costList) {
        this.rentType = rentType;
        this.costList = costList;
    }

    public PriceOverviewVO(String cityName, String cityCode, String rentType, List<CarGroupCostVO> costList) {
        this.cityName = cityName;
        this.cityCode = cityCode;
        this.costList = costList;
        this.rentType = rentType;

    }
}
