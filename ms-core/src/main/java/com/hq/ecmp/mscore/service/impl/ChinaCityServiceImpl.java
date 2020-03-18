package com.hq.ecmp.mscore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.ecmp.mscore.mapper.ChinaCityMapper;
import com.hq.ecmp.mscore.service.ChinaCityService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChinaCityServiceImpl implements ChinaCityService {

	@Autowired
	ChinaCityMapper chinaCityMapper;

	@Override
	public String queryCityCodeByCityName(String cityName) {
		return chinaCityMapper.queryCityCodeByCityName(cityName);
	}

}
