package com.hq.ecmp.mscore.bo;

import lombok.Data;

/**
 *
 * 地理定位 地址信息,用于接收 三方平台地址反查 结果
 * @Author: zj.hu
 * @Date: 2020-01-04 13:51
 */
@Data
public class LbsAddress {

    /**
     * 端地址
     */
    private String shortName;

    /**
     * 长地址
     */
    private String longName;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 省份
     */
    private int province;

    /**
     * 市区
     */
    private int city;

    /**
     * 区
     */
    private int area;

    /**
     * 街道地址
     */
    private int street;
}
