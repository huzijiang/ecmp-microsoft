package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.bo.CityInfo;
import com.hq.ecmp.mscore.domain.DriverCreateInfo;
import com.hq.ecmp.mscore.domain.DriverInfo;
import com.hq.ecmp.mscore.domain.DriverQuery;
import com.hq.ecmp.mscore.domain.DriverQueryResult;
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
      
      public List<CityInfo> queryCityInfoListByCityName(@Param("cityName")String cityName);
}
