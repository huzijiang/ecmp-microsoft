package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xueyong
 * @date 2020/1/4
 * ecmp-proxy.
 */
@Data
@ApiModel(description = "省份城市模型")
public class ProvinceCityVO {

    @ApiModelProperty(name = "provinceCode",value = "省份代码")
    private String provinceCode;
    @ApiModelProperty(name = "cityCode",value = "城市代码")
    private String cityCode;
    @ApiModelProperty(name = "cityName",value = "城市名称")
    private String cityName;


    @Override
    public String toString() {
        return "ProvinceCityVO{" +
                "provinceCode='" + provinceCode + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", cityName='" + cityName + '\'' +
                '}';
    }
}
