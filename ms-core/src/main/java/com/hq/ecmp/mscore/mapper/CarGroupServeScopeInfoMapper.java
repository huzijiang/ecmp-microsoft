package com.hq.ecmp.mscore.mapper;





import java.util.List;

import com.hq.ecmp.mscore.domain.CarGroupServeScopeInfo;
import org.apache.ibatis.annotations.Param;
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

	/**
	 * 通过ID查询单条数据
	 *
	 * @param id 主键
	 * @return 实例对象
	 */
	CarGroupServeScopeInfo queryById(Long id);

	/**
	 * 查询指定行数据
	 *
	 * @param offset 查询起始位置
	 * @param limit 查询条数
	 * @return 对象列表
	 */
	List<CarGroupServeScopeInfo> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


	/**
	 * 通过实体作为筛选条件查询
	 *
	 * @param carGroupServeScopeInfo 实例对象
	 * @return 对象列表
	 */
	List<CarGroupServeScopeInfo> queryAll(CarGroupServeScopeInfo carGroupServeScopeInfo);

	/**
	 * 新增数据
	 *
	 * @param carGroupServeScopeInfo 实例对象
	 * @return 影响行数
	 */
	int insert(CarGroupServeScopeInfo carGroupServeScopeInfo);

	/**
	 * 修改数据
	 *
	 * @param carGroupServeScopeInfo 实例对象
	 * @return 影响行数
	 */
	int update(CarGroupServeScopeInfo carGroupServeScopeInfo);

	/**
	 * 通过主键删除数据
	 *
	 * @param id 主键
	 * @return 影响行数
	 */
	int deleteById(Long id);

	/**
	 * 修改车队服务地址信息
	 * @param carGroupServeScopeInfo
	 * @return
	 */
	int updateInfo(CarGroupServeScopeInfo carGroupServeScopeInfo);

	/**
	 * 通过车队ID删除数据
	 *
	 * @param carGroupId 车队ID
	 * @return 影响行数
	 */
	int deleteByCarGroupId(Long carGroupId);
}
