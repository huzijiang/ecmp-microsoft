package com.hq.ecmp.mscore.mapper;
import com.hq.ecmp.mscore.vo.ProvinceCityVO;
import com.hq.ecmp.mscore.vo.ProvinceVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
@Repository
public interface ChinaProvinceMapper
{


      public List<ProvinceVO> queryProvince();
      public List<ProvinceCityVO> queryCityByProvince(String provinceCode);


}
