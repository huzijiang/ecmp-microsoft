package com.hq.ecmp.mscore.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName CarLocationDto
 * @Description TODO
 * @Author yj
 * @Date 2020/3/18 10:09
 * @Version 1.0
 */
@ApiModel(value = "车辆检索入参model")
@Data
public class CarLocationDto {

    @ApiModelProperty(value = "城市id")
    private String cityId;
    @ApiModelProperty(value = "车型类别code,\n" +
            "P001-公务级\n" +
            "P002-行政级\n" +
            "P003-六座商务")
    private String carLevel;
    @ApiModelProperty(value = "车牌号")
    private String carLicense;
    @ApiModelProperty(value = "能源类型,\n" +
            "P001   汽油\n" +
            "P002   柴油\n" +
            "P003   电力\n" +
            "P004   混合")
    private String powerType;
    @ApiModelProperty(value = "资产编号")
    private Long assetTag;
    @ApiModelProperty(value = "车辆性质, S001   购买\n" +
            "s002   租赁\n" +
            "S003   借调")
    private String carSource;
    @ApiModelProperty(value = "汽车状态，\n" +
            "S000    启用中\n" +
            "S001    禁用中\n" +
            "S002    维护中\n" +
            "S003    已到期\n" +
            "S101    被借调\n")
    private String carState;

    /**
     * 车队id
     */
    private List<Long> carGroupIds;




}
