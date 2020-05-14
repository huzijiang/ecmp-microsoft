package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.vo.ProvinceCityVO;
import com.hq.ecmp.mscore.vo.ProvinceVO;

import java.util.List;

public interface ChinaProvinceService {
	public List<ProvinceVO> queryProvince();
	public List<ProvinceCityVO> queryCityByProvince(String provinceCode);

}
