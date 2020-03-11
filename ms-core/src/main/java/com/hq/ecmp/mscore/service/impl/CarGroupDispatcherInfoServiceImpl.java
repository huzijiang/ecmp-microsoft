package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.CarGroupDispatcherInfo;
import com.hq.ecmp.mscore.mapper.CarGroupDispatcherInfoMapper;
import com.hq.ecmp.mscore.service.ICarGroupDispatcherInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class CarGroupDispatcherInfoServiceImpl implements ICarGroupDispatcherInfoService
{
    @Autowired
    private CarGroupDispatcherInfoMapper carGroupDispatcherInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param dispatcherId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public CarGroupDispatcherInfo selectCarGroupDispatcherInfoById(Long dispatcherId)
    {
        return carGroupDispatcherInfoMapper.selectCarGroupDispatcherInfoById(dispatcherId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carGroupDispatcherInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CarGroupDispatcherInfo> selectCarGroupDispatcherInfoList(CarGroupDispatcherInfo carGroupDispatcherInfo)
    {
        return carGroupDispatcherInfoMapper.selectCarGroupDispatcherInfoList(carGroupDispatcherInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param carGroupDispatcherInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCarGroupDispatcherInfo(CarGroupDispatcherInfo carGroupDispatcherInfo)
    {
        carGroupDispatcherInfo.setCreateTime(DateUtils.getNowDate());
        return carGroupDispatcherInfoMapper.insertCarGroupDispatcherInfo(carGroupDispatcherInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param carGroupDispatcherInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCarGroupDispatcherInfo(CarGroupDispatcherInfo carGroupDispatcherInfo)
    {
        carGroupDispatcherInfo.setUpdateTime(DateUtils.getNowDate());
        return carGroupDispatcherInfoMapper.updateCarGroupDispatcherInfo(carGroupDispatcherInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param dispatcherIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarGroupDispatcherInfoByIds(Long[] dispatcherIds)
    {
        return carGroupDispatcherInfoMapper.deleteCarGroupDispatcherInfoByIds(dispatcherIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param dispatcherId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarGroupDispatcherInfoById(Long dispatcherId)
    {
        return carGroupDispatcherInfoMapper.deleteCarGroupDispatcherInfoById(dispatcherId);
    }

	@Override
	public List<Long> queryCarGroupIdList(Long userId) {
		return carGroupDispatcherInfoMapper.queryCarGroupIdList(userId);
	}

	@Override
	public List<Long> queryUserByCarGroup(List<Long> list) {
		return carGroupDispatcherInfoMapper.queryUserByCarGroup(list);
	}
}
