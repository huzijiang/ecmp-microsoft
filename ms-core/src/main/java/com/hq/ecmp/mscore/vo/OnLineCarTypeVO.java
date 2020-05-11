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
public class OnLineCarTypeVO {

    private Long cityId;
    private String cityCode;
    private String cityName;
    private String carGroupIds;
    private List<CarLevelVO> carType;
    private List<CarServiceTypeVO> serviceType;

    public OnLineCarTypeVO() {
    }

    public OnLineCarTypeVO(Long cityId, String cityCode, String cityName, String carGroupIds) {
        this.cityId = cityId;
        this.cityCode = cityCode;
        this.cityName = cityName;
        this.carGroupIds = carGroupIds;
    }
}
