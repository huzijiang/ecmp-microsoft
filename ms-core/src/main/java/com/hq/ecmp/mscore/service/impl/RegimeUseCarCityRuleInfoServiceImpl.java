package com.hq.ecmp.mscore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.ecmp.mscore.domain.RegimeUseCarCityRuleInfo;
import com.hq.ecmp.mscore.mapper.RegimeUseCarCityRuleInfoMapper;
import com.hq.ecmp.mscore.service.IRegimeUseCarCityRuleInfoService;



@Service
public class RegimeUseCarCityRuleInfoServiceImpl implements IRegimeUseCarCityRuleInfoService {
	@Autowired
	RegimeUseCarCityRuleInfoMapper regimeUseCarCityRuleInfoMapper;

	@Override
	public Integer insert(RegimeUseCarCityRuleInfo regimeUseCarCityRuleInfo) {
		return regimeUseCarCityRuleInfoMapper.insert(regimeUseCarCityRuleInfo);
	}

	@Override
	public Integer batchInsert(List<RegimeUseCarCityRuleInfo> list) {
		return regimeUseCarCityRuleInfoMapper.batchInsert(list);
	}
	
	
	
	

}
