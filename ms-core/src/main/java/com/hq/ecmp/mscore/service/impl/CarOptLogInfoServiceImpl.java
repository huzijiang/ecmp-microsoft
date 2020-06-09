package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.CarOptLogInfo;
import com.hq.ecmp.mscore.mapper.CarOptLogInfoMapper;
import com.hq.ecmp.mscore.service.ICarOptLogInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author hqer
 * @date 2020-06-09
 */
@Service
public class CarOptLogInfoServiceImpl implements ICarOptLogInfoService
{
    @Autowired
    private CarOptLogInfoMapper carOptLogInfoMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param logId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public CarOptLogInfo selectCarOptLogInfoById(Long logId)
    {
        return carOptLogInfoMapper.selectCarOptLogInfoById(logId);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param carOptLogInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CarOptLogInfo> selectCarOptLogInfoList(CarOptLogInfo carOptLogInfo)
    {
        return carOptLogInfoMapper.selectCarOptLogInfoList(carOptLogInfo);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param carOptLogInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCarOptLogInfo(CarOptLogInfo carOptLogInfo)
    {
        carOptLogInfo.setCreateTime(DateUtils.getNowDate());
        return carOptLogInfoMapper.insertCarOptLogInfo(carOptLogInfo);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param carOptLogInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCarOptLogInfo(CarOptLogInfo carOptLogInfo)
    {
        carOptLogInfo.setUpdateTime(DateUtils.getNowDate());
        return carOptLogInfoMapper.updateCarOptLogInfo(carOptLogInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param logIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarOptLogInfoByIds(Long[] logIds)
    {
        return carOptLogInfoMapper.deleteCarOptLogInfoByIds(logIds);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param logId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarOptLogInfoById(Long logId)
    {
        return carOptLogInfoMapper.deleteCarOptLogInfoById(logId);
    }
}