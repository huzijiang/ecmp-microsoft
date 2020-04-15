package com.hq.ecmp.mscore.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.hq.ecmp.mscore.domain.RegimeUseCarTimeRuleInfo;


@Repository
public interface RegimeUseCarTimeRuleInfoMapper
{
   
        public Integer batchInsert(@Param("list")List<RegimeUseCarTimeRuleInfo> list);
        
        
        public List<RegimeUseCarTimeRuleInfo> queryRegimeUseCarTimeRuleInfoList(Long regimenId);


}
