package com.hq.ecmp.mscore.service.impl;

import java.util.List;

import com.hq.ecmp.mscore.domain.RegimeVo;
import com.hq.ecmp.mscore.mapper.RegimeInfoMapper;
import com.hq.ecmp.mscore.mapper.RegimeUseCarCityRuleInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.ecmp.mscore.vo.CityInfo;
import com.hq.ecmp.mscore.mapper.ChinaCityMapper;
import com.hq.ecmp.mscore.service.ChinaCityService;

import lombok.extern.slf4j.Slf4j;

import static com.hq.ecmp.constant.CommonConstant.*;

@Service
@Slf4j
public class ChinaCityServiceImpl implements ChinaCityService {

	@Autowired
	ChinaCityMapper chinaCityMapper;
	@Autowired
	RegimeInfoMapper regimeInfoMapper;
	@Autowired
	RegimeUseCarCityRuleInfoMapper regimeUseCarCityRuleInfoMapper;


	@Override
	public String queryCityCodeByCityName(String cityName) {
		return chinaCityMapper.queryCityCodeByCityName(cityName);
	}

	@Override
	public List<CityInfo> queryCityInfoListByCityName(String cityName,Long regimeId) {
		RegimeVo regimeVo = regimeInfoMapper.queryRegimeDetail(regimeId);
		//flag:1可用城市,0:不可用城市
		int flag=1;
		List<String> queryLimitCityCodeList=null;
		if (regimeVo!=null){
			String ruleCity = regimeVo.getRuleCity();
			if (ROLE_CITY_C002.equals(ruleCity)){
				queryLimitCityCodeList = regimeUseCarCityRuleInfoMapper.queryLimitCityCodeList(regimeId);
			}else if (ROLE_CITY_C003.equals(ruleCity)){
				flag=0;
				queryLimitCityCodeList = regimeUseCarCityRuleInfoMapper.queryLimitCityCodeList(regimeId);
			}
		}
		return chinaCityMapper.queryCityInfoListByCityName(cityName,queryLimitCityCodeList,flag);
	}

	@Override
	public CityInfo queryCityByCityCode(String cityCode) {
		return chinaCityMapper.queryCityByCityCode(cityCode);
	}

}
