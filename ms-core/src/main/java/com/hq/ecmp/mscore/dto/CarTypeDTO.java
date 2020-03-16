package com.hq.ecmp.mscore.dto;

import lombok.Data;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/15 16:10
 */
@Data
public class CarTypeDTO {

    private Long carTypeId;

    private Long enterpriseId;  //企业id

    private Long countryCarTypeId;  // 暂用不着

    private String name;  // 车型名称

    private String level; //车型级别代码,一个车可以归属为多个级别  P001-公务级  P002-行政级  P003-六座商务

}
