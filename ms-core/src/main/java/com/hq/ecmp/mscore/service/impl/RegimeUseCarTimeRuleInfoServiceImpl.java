package com.hq.ecmp.mscore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.ecmp.mscore.domain.RegimeUseCarTimeRuleInfo;
import com.hq.ecmp.mscore.mapper.RegimeUseCarTimeRuleInfoMapper;
import com.hq.ecmp.mscore.service.IRegimeUseCarTimeRuleInfoService;



@Service
public class RegimeUseCarTimeRuleInfoServiceImpl implements IRegimeUseCarTimeRuleInfoService {
	@Autowired
	RegimeUseCarTimeRuleInfoMapper regimeUseCarTimeRuleInfoMapper;

	@Override
	public Integer batchInsert(List<RegimeUseCarTimeRuleInfo> list) {
		return regimeUseCarTimeRuleInfoMapper.batchInsert(list);
	}

	@Override
	public List<RegimeUseCarTimeRuleInfo> queryRegimeUseCarTimeRuleInfoList(Long regimenId) {
		
		return regimeUseCarTimeRuleInfoMapper.queryRegimeUseCarTimeRuleInfoList(regimenId);
	}

	
	
	
	
	

}
