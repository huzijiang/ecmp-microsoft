package com.hq.ecmp.mscore.service.impl;

import com.hq.ecmp.mscore.bo.CityInfo;
import com.hq.ecmp.mscore.mapper.ChinaCityMapper;
import com.hq.ecmp.mscore.mapper.ChinaProvinceMapper;
import com.hq.ecmp.mscore.service.ChinaCityService;
import com.hq.ecmp.mscore.service.ChinaProvinceService;
import com.hq.ecmp.mscore.vo.ProvinceCityVO;
import com.hq.ecmp.mscore.vo.ProvinceVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ChinaProvinceServiceImpl implements ChinaProvinceService {

	@Autowired
	private ChinaProvinceMapper chinaProvinceMapper;
	@Override
	public List<ProvinceVO> queryProvince(){
		return chinaProvinceMapper.queryProvince();
	}
	@Override
	public List<ProvinceCityVO> queryCityByProvince(String provinceCode){
		return chinaProvinceMapper.queryCityByProvince(provinceCode);
	}



}
