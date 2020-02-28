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
@ApiModel(description = "地址模型")
public class AddressVO {

    /**
     * 长地址
     */
    @ApiModelProperty(name = "longAddress",value = "长地址")
    private String longAddress;

    /**
     * 短地址（具体地址）
     */
    @ApiModelProperty(name = "address",value = "具体地点")
    private String address;

    /**
     * 地点坐标
     */
    @ApiModelProperty(name = "addressPoint",value = "地点坐标")
    private String addressPoint;


    /**
     * 下车地址
     */
    @ApiModelProperty(name = "longitude",value = "经度")
    private String longitude;

}
