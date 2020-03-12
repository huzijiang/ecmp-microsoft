package com.hq.ecmp.mscore.mapper;





import java.util.List;

import org.springframework.stereotype.Repository;



@Repository
public interface CarGroupServeScopeInfoMapper
{
	/**
	 *查询城市里面有的车队
	 * @param city
	 * @return
	 */
    public List<Long> queryCarGroupByCity(String city);
}
