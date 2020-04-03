package com.hq.ecmp.mscore.service;

import java.util.List;

import com.hq.ecmp.mscore.domain.RegimeUseCarTimeRuleInfo;

public interface IRegimeUseCarTimeRuleInfoService {
	  
	  public Integer batchInsert(List<RegimeUseCarTimeRuleInfo> list);
	  
	  
	  public List<RegimeUseCarTimeRuleInfo>  queryRegimeUseCarTimeRuleInfoList(Long regimenId);
}
