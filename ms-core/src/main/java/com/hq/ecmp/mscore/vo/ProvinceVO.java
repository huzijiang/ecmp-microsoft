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
@ApiModel(description = "省份模型")
public class ProvinceVO {

    @ApiModelProperty(name = "provinceCode",value = "省份代码")
    private String provinceCode;
    @ApiModelProperty(name = "provinceName",value = "省份名称")
    private String provinceName;


    @Override
    public String toString() {
        return "ProvinceVO{" +
                "provinceCode='" + provinceCode + '\'' +
                ", provinceName='" + provinceName + '\'' +
                '}';
    }
}
