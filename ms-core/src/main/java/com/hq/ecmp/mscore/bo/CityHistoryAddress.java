package com.hq.ecmp.mscore.bo;


import lombok.Data;

@Data
public class CityHistoryAddress {

    /***
     * 历史地址id
     */
    private Long historyId;

    /***
     * 用户id
     */
    private Long userId;


    /***
     * 城市code码
     */
    private String cityCode;


    /***
     * 短地址
     */
    private String shortName;

    /***
     * 长地址
     */
    private String address;

    /***
     * 省份
     */
    private String province;

    /***
     * 完整地址
     */
    private String fullName;

    /***
     * 经纬度 【经度,纬度】
     */
    private String poi;

    /***
     * 市名
     */
    private String cityName;

    /***
     * 纬度
     */
    private String lat;

    /***
     * 经度
     */
    private String lng;

}
