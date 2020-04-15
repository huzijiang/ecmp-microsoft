package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author xueyong
 * @date 2020/1/6
 * ecmp-proxy.
 */
@Data
@ApiModel(description = "差旅用车行程对象")
public class TravelRequest {

    /**
     * 交通工具
     */
    @ApiModelProperty(name = "vehicle", value = "交通工具")
    private String vehicle;
    /**
     * 开始城市
     */
    @ApiModelProperty(name = "startCity", value = "交通工具")
    private CityInfo startCity;
    /**
     * 结束城市
     */
    @ApiModelProperty(name = "endCity", value = "结束城市")
    private CityInfo endCity;
    /**
     * 开始时间
     */
    @ApiModelProperty(name = "startDate", value = "开始时间")
    private Date startDate;
    /**
     * 结束时间
     */
    @ApiModelProperty(name = "endDate", value = "结束时间")
    private Date endDate;
    /**
     * 出差天数
     */
    @ApiModelProperty(name = "countDate", value = "出差天数")
    private Integer countDate;
}
