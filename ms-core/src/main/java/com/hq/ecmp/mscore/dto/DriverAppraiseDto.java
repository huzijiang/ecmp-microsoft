package com.hq.ecmp.mscore.dto;

import lombok.Data;

/**
 * @Author: chao.zhang
 * @Date: 2020/6/11 13:37
 */
@Data
public class DriverAppraiseDto {

    private Integer pageNum;
    private Integer pageSize;
    private Long carId;
    private Long driverId;
}
