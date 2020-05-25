package com.hq.ecmp.mscore.vo;

import com.hq.core.aspectj.lang.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xueyong
 * @date 2020/1/4
 * ecmp-proxy.
 */
@Data
@ApiModel(description = "地址模型")
public class CarGroupCostVO {

    @ApiModelProperty(name = "amount",value = "车费预估-优惠后金额")
    private Long carGroupId;
    private Long costId;
    private String itIsInner;
    private String telephone;
    private String cityCode;
    private String cityName;
    private String carGroupUserMode;
    private String rentType;
    private String carTypeId;
    private String carTypeName;
    private String carGroupName;
    @ApiModelProperty(name = "combosPrice",value = "套餐价")
    private BigDecimal combosPrice;
    @ApiModelProperty(name = "combosMileage",value = "套餐里程")
    private BigDecimal combosMileage;
    @ApiModelProperty(name = "combosTimes",value = "套餐包含时长/分钟")
    private Long combosTimes;
    @ApiModelProperty(name = "beyondPriceEveryKm",value = "超里程价格（单位  /每千米）")
    private BigDecimal beyondPriceEveryKm;
    @ApiModelProperty(name = "beyondPriceEveryMinute",value = "超时费用（单位：每分钟)")
    private BigDecimal beyondPriceEveryMinute;

}
