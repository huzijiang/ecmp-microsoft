package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.vo.CityInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
@Repository
public interface ChinaCityMapper
{
      public String queryCityCodeByCityName(String cityName);
      
      public List<CityInfo> queryCityInfoListByCityName(@Param("cityName")String cityName,@Param("citys")List<String> citys,@Param("flag")int flag);

    CityInfo queryCityByCityCode(String cityCode);

    List<CityInfo> findByCityCode(@Param("cityCode")String cityCode);
}
