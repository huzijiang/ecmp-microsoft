package com.hq.ecmp.mscore.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.hq.ecmp.mscore.domain.RegimeUseCarCityRuleInfo;


@Repository
public interface RegimeUseCarCityRuleInfoMapper
{
   
        public Integer insert(RegimeUseCarCityRuleInfo regimeUseCarCityRuleInfo);
        
        public Integer batchInsert(@Param("list")List<RegimeUseCarCityRuleInfo> list);


}
