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
     * 上车地址
     */
    @ApiModelProperty(name = "startAddrPoint",value = "上车地点坐标")
    private String startAddrPoint;
    /**
     * 上车坐标
     */
    @ApiModelProperty(name = "startAddr",value = "上车地点")
    private String startAddr;
    /**
     * 下车坐标
     */
    @ApiModelProperty(name = "endAddrPoint",value = "下车地点坐标")
    private String endAddrPoint;
    /**
     * 下车地址
     */
    @ApiModelProperty(name = "endAddr",value = "下车地点")
    private String endAddr;

}
