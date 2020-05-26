package com.hq.ecmp.mscore.vo;

import com.hq.core.aspectj.lang.annotation.Excel;
import com.hq.ecmp.mscore.domain.CostConfigInfo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @auther: zj.hu
 * @date: 2020-05-26
 */
@Data
public class CostConfigDetailInfoVo extends CostConfigInfo {


    private Long costId;


    private Long companyId;


    private String costConfigName;


    private String serviceType;


    private String rentType;


    private BigDecimal startPrice;


    private BigDecimal combosPrice;


    private BigDecimal combosMileage;


    private Long combosTimes;


    private BigDecimal beyondPriceEveryKm;


    private BigDecimal beyondPriceEveryMinute;


    private BigDecimal waitPriceEreryMinute;


    private String state;


    private String carGroupUserMode;


    //车队编号
    private Long  carGroupId;

    //城市代码
    private String cityCode;

    //车型级别编号
    private String carTypeId;

    //车型级别代码
    private String carTypeLevel;





}
