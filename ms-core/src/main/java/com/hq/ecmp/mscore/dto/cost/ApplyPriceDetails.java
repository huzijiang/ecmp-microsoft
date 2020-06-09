package com.hq.ecmp.mscore.dto.cost;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用车申请
 */
@ApiModel(value = "用车申请价格详情model")
@Data
public class ApplyPriceDetails {

    @ApiModelProperty(value = "成本设置id")
    private Long costId;
    @ApiModelProperty(value = "公司id")
    private Long companyId;
    private String state;
    @ApiModelProperty(value = "用车天数")
    private String applyDays;
    @ApiModelProperty(value = "成本名称")
    private String costConfigName;
    @ApiModelProperty(value = "车队id")
    private Long carGroupId;
    @ApiModelProperty(value = "车队名称")
    private String carGroupName;
    @ApiModelProperty(value = "城市名称")
    private String cityName;
    @ApiModelProperty(value = "城市code")
    private String cityCode;
    @ApiModelProperty(value = "服务类型")
    private String serviceType;
    @ApiModelProperty(value = "服务模式")
    private String carGroupUserMode;
    @ApiModelProperty(value = "包车类型，T001  半日租\n" +
            "T002  整日租 ")
    private String rentType;
    @ApiModelProperty("车型")
    private String  carTypeName;
    @ApiModelProperty(value = "起步价，非包车类型使用")
    private BigDecimal startPrice;
    @ApiModelProperty(value = "套餐价，包车类型使用")
    private BigDecimal combosPrice;
    @ApiModelProperty(value = "套餐里程（：公里）")
    private BigDecimal combosMileage;
    @ApiModelProperty(value = "套餐时长（：分钟）")
    private Long combosTimes;
    @ApiModelProperty(value = "超里程单价（元/公里）")
    private BigDecimal beyondPriceEveryKm;
    @ApiModelProperty(value = "超里程时长（元/分钟）")
    private BigDecimal beyondPriceEveryMinute;
    @ApiModelProperty(value = "等待费（元/分钟）")
    private BigDecimal waitPriceEreryMinute;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone ="GMT+8" )
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone ="GMT+8" )
    private Date updateTime;
    @ApiModelProperty(value = "是否自驾")
    private String itIsSelfDriver;
    @ApiModelProperty(value = "车型id")
    private  Long carTypeId;
}
