package com.hq.ecmp.mscore.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/17 8:35
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarTypeVO {

    private Long carTypeId;

    private Long enterpriseId;  //企业id

    private Long countryCarTypeId;  // 暂用不着

    private String name;  // 车型名称

    private String level; //车型级别代码,一个车可以归属为多个级别  P001-公务级  P002-行政级  P003-六座商务

    private String status;

    private String carType; //车型描述

    private String carNum; //总车辆数

    private String  imageUrl; //车型图标

    private Integer sort;
}
