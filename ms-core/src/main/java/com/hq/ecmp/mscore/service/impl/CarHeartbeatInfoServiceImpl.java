package com.hq.ecmp.mscore.service.impl;

import java.util.List;

import com.hq.ecmp.mscore.domain.CarHeartbeatInfo;
import com.hq.ecmp.mscore.mapper.CarHeartbeatInfoMapper;
import com.hq.ecmp.mscore.service.ICarHeartbeatInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class CarHeartbeatInfoServiceImpl implements ICarHeartbeatInfoService
{
    @Autowired
    private CarHeartbeatInfoMapper carHeartbeatInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param carHeartbeatId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public CarHeartbeatInfo selectCarHeartbeatInfoById(String carHeartbeatId)
    {
        return carHeartbeatInfoMapper.selectCarHeartbeatInfoById(carHeartbeatId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carHeartbeatInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CarHeartbeatInfo> selectCarHeartbeatInfoList(CarHeartbeatInfo carHeartbeatInfo)
    {
        return carHeartbeatInfoMapper.selectCarHeartbeatInfoList(carHeartbeatInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param carHeartbeatInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCarHeartbeatInfo(CarHeartbeatInfo carHeartbeatInfo)
    {
        return carHeartbeatInfoMapper.insertCarHeartbeatInfo(carHeartbeatInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param carHeartbeatInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCarHeartbeatInfo(CarHeartbeatInfo carHeartbeatInfo)
    {
        return carHeartbeatInfoMapper.updateCarHeartbeatInfo(carHeartbeatInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param carHeartbeatIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarHeartbeatInfoByIds(String[] carHeartbeatIds)
    {
        return carHeartbeatInfoMapper.deleteCarHeartbeatInfoByIds(carHeartbeatIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param carHeartbeatId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarHeartbeatInfoById(String carHeartbeatId)
    {
        return carHeartbeatInfoMapper.deleteCarHeartbeatInfoById(carHeartbeatId);
    }
}
