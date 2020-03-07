package com.hq.ecmp.mscore.vo;

import lombok.Data;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/4 15:36
 */
@Data
public class TravelPickupCity {

    //城市名字
    private String cityName;
    //接机/站次数
    private Integer pickup;
    //送机/站次数
    private Integer dropOff;

}
