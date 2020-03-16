package com.hq.ecmp.mscore.service;

import java.util.List;

import com.hq.ecmp.mscore.domain.RegimeUseCarCityRuleInfo;

public interface IRegimeUseCarCityRuleInfoService {
	  public Integer insert(RegimeUseCarCityRuleInfo regimeUseCarCityRuleInfo);
	  
	  public Integer batchInsert(List<RegimeUseCarCityRuleInfo> list);
}
