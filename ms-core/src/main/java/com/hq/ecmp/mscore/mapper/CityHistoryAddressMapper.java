package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.bo.CityHistoryAddress;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityHistoryAddressMapper {


     /***
     * 添加当前城市地址记录
     * @param list
     * @return
     * @throws Exception
     */
     public int addCityAddress(List<CityHistoryAddress> list)throws Exception;



     /***
     * 查询当前城市地址记录
     * @param cityHistoryAddress
     * @return
     * @throws Exception
     */
     public List<CityHistoryAddress> getCityAddress(CityHistoryAddress cityHistoryAddress)throws Exception;


     /***
      *
      * @param cityHistoryAddress
      * @return
      * @throws Exception
      */
     public List<CityHistoryAddress> getCityAddressList(CityHistoryAddress cityHistoryAddress)throws Exception;


}

