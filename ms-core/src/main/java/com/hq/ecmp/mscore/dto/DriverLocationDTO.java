package com.hq.ecmp.mscore.dto;

import lombok.Data;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/9 15:12
 */
@Data
public class DriverLocationDTO {

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;
    private Long orderId;

}
