package com.hq.ecmp.mscore.service.impl;

import com.hq.ecmp.mscore.bo.CarTrackInfo;
import com.hq.ecmp.mscore.mapper.CarTrackInfoMapper;
import com.hq.ecmp.mscore.service.ICarTrackInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 车辆轨迹信息Service业务层处理
 * 
 * @author hqer
 * @date 2020-06-02
 */
@Service
public class CarTrackInfoServiceImpl implements ICarTrackInfoService
{
    @Autowired
    private CarTrackInfoMapper carTrackInfoMapper;

    /**
     * 查询车辆轨迹信息
     * 
     * @param id 车辆轨迹信息ID
     * @return 车辆轨迹信息
     */
    @Override
    public CarTrackInfo selectCarTrackInfoById(Long id)
    {
        return carTrackInfoMapper.selectCarTrackInfoById(id);
    }

    /**
     * 查询车辆轨迹信息列表
     * 
     * @param carTrackInfo 车辆轨迹信息
     * @return 车辆轨迹信息
     */
    @Override
    public List<CarTrackInfo> selectCarTrackInfoList(CarTrackInfo carTrackInfo)
    {
        return carTrackInfoMapper.selectCarTrackInfoList(carTrackInfo);
    }

    /**
     * 新增车辆轨迹信息
     * 
     * @param carTrackInfo 车辆轨迹信息
     * @return 结果
     */
    @Override
    public int insertCarTrackInfo(CarTrackInfo carTrackInfo)
    {
        return carTrackInfoMapper.insertCarTrackInfo(carTrackInfo);
    }

    /**
     * 修改车辆轨迹信息
     * 
     * @param carTrackInfo 车辆轨迹信息
     * @return 结果
     */
    @Override
    public int updateCarTrackInfo(CarTrackInfo carTrackInfo)
    {
        return carTrackInfoMapper.updateCarTrackInfo(carTrackInfo);
    }

    /**
     * 批量删除车辆轨迹信息
     * 
     * @param ids 需要删除的车辆轨迹信息ID
     * @return 结果
     */
    @Override
    public int deleteCarTrackInfoByIds(Long[] ids)
    {
        return carTrackInfoMapper.deleteCarTrackInfoByIds(ids);
    }

    /**
     * 删除车辆轨迹信息信息
     * 
     * @param id 车辆轨迹信息ID
     * @return 结果
     */
    @Override
    public int deleteCarTrackInfoById(Long id)
    {
        return carTrackInfoMapper.deleteCarTrackInfoById(id);
    }
}