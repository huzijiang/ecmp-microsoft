package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.CarMaintenanceInfo;
import com.hq.ecmp.mscore.mapper.CarMaintenanceInfoMapper;
import com.hq.ecmp.mscore.service.ICarMaintenanceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class CarMaintenanceInfoServiceImpl implements ICarMaintenanceInfoService
{
    @Autowired
    private CarMaintenanceInfoMapper carMaintenanceInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param maintenanceId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public CarMaintenanceInfo selectCarMaintenanceInfoById(Long maintenanceId)
    {
        return carMaintenanceInfoMapper.selectCarMaintenanceInfoById(maintenanceId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carMaintenanceInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CarMaintenanceInfo> selectCarMaintenanceInfoList(CarMaintenanceInfo carMaintenanceInfo)
    {
        return carMaintenanceInfoMapper.selectCarMaintenanceInfoList(carMaintenanceInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param carMaintenanceInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCarMaintenanceInfo(CarMaintenanceInfo carMaintenanceInfo)
    {
        carMaintenanceInfo.setCreateTime(DateUtils.getNowDate());
        return carMaintenanceInfoMapper.insertCarMaintenanceInfo(carMaintenanceInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param carMaintenanceInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCarMaintenanceInfo(CarMaintenanceInfo carMaintenanceInfo)
    {
        carMaintenanceInfo.setUpdateTime(DateUtils.getNowDate());
        return carMaintenanceInfoMapper.updateCarMaintenanceInfo(carMaintenanceInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param maintenanceIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarMaintenanceInfoByIds(Long[] maintenanceIds)
    {
        return carMaintenanceInfoMapper.deleteCarMaintenanceInfoByIds(maintenanceIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param maintenanceId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarMaintenanceInfoById(Long maintenanceId)
    {
        return carMaintenanceInfoMapper.deleteCarMaintenanceInfoById(maintenanceId);
    }
}
