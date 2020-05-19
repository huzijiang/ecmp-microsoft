package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.bo.CityHistoryAddress;

import java.util.List;

public interface CityHistoryAddressService {


    /***
     *
     * @param userId
     * @param list
     * @return
     * @throws Exception
     */
    public int addCityAddress(Long userId, List<CityHistoryAddress> list)throws Exception;


    /***
     *
     * @param userId
     * @param cityHistoryAddress
     * @return
     */
    public List<CityHistoryAddress> getCityAddress(Long userId,String cityCode,String cityName,String shortAddress)throws Exception;


    /***
     *
     * @param cityCOde
     * @return
     */
    public  List<CityHistoryAddress> gethotcityaddress(String cityCOde)throws Exception;


}
