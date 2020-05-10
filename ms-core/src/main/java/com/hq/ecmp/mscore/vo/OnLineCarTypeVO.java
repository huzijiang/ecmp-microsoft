package com.hq.ecmp.mscore.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: caobj
 * @Date: 2020/3/14 13:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnLineCarTypeVO {

    private Long cityId;
    private String cityCode;
    private String cityName;
    private String carGroupIds;
    private List<CarLevelVO> carType;
    private List<CarServiceTypeVO> serviceType;
}
