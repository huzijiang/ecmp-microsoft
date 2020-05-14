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

    private String cityId;
    private String cityName;
    private String carGroupIds;
    private List<CarLevelVO> carTypes;
    private List<CarServiceTypeVO> serviceTypes;

    public OnLineCarTypeVO() {
    }

    public OnLineCarTypeVO(String cityId, String cityName, String carGroupIds) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.carGroupIds = carGroupIds;
    }
}
