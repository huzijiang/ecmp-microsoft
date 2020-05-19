package com.hq.ecmp.mscore.service.impl;


import com.google.gson.reflect.TypeToken;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.MacTools;
import com.hq.common.utils.OkHttpUtil;
import com.hq.ecmp.mscore.bo.CityHistoryAddress;
import com.hq.ecmp.mscore.mapper.CityHistoryAddressMapper;
import com.hq.ecmp.mscore.service.CityHistoryAddressService;
import com.hq.ecmp.util.GsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/***
 * add by liuzh 2020/05/07城市历史地址记录逻辑层
 */

@Service
public class CityHistoryAddressServiceImpl implements CityHistoryAddressService {


    @Autowired
    private CityHistoryAddressMapper cityHistoryAddressMapper;

    /***
     * 添加当前城市地址逻辑函数（个人）
     * @param userId
     * @param list
     * @return
     * @throws Exception
     */

    @Override
    public int addCityAddress(Long userId,List<CityHistoryAddress> list) throws Exception{
       if(null!= userId && userId > 0 && null!=list && list.size()>0){
           for(int i=0;i<list.size();i++){
               list.get(i).setUserId(userId);
               list.get(i).setHistoryId(null);
               if(cityHistoryAddressMapper.getCityAddress(list.get(i)).size()>0){
                   list.remove(list.get(i));
               }
           }
           if(list.size()==0){
               return 1;
           }
           return cityHistoryAddressMapper.addCityAddress(list);
       }
       return 0;
    }


    /***
     * 获取当前城市历史地址函数（个人）
     * @param userId
     * @param cityHistoryAddress
     * @return
     * @throws Exception
     */
     @Override
     public List<CityHistoryAddress> getCityAddress(Long userId,String cityCode,String cityName,String shortAddress) throws Exception{
            if(null!= userId && userId > 0 ){
                CityHistoryAddress cityHistoryAddress = new CityHistoryAddress();
                cityHistoryAddress.setUserId(userId);
                cityHistoryAddress.setCityCode(cityCode);
                List<CityHistoryAddress> list = cityHistoryAddressMapper.getCityAddressList(cityHistoryAddress);
                if(null!=list && list.size()>0){
                    return list;
                }
            }
            return null;
     }


     /***
     * 获取云端历史地址函数
     * @param cityCOde
     * @return
     * @throws Exception
     */
     @Override
     public List<CityHistoryAddress> gethotcityaddress(String cityCOde) throws Exception{
        return null;
     }
}

