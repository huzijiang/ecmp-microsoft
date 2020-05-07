package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 城市VO
 * @author xueyong
 * @date 2019/12/31
 * ecmp-proxy.
 */
@Data
@ApiModel(description = "城市模型")
public class
CityInfo {

    private Integer cityId;

    @ApiModelProperty(name = "cityName",value = "城市名称")
    private String cityName;                 // TODO 由Integer改为String

    @ApiModelProperty(name = "cityCode",value = "城市code码")
    private String cityCode;
    private String provinceCode;

}
