package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.CarYearlyCheckInfo;
import com.hq.ecmp.mscore.mapper.CarYearlyCheckInfoMapper;
import com.hq.ecmp.mscore.service.ICarYearlyCheckInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class CarYearlyCheckInfoServiceImpl implements ICarYearlyCheckInfoService
{
    @Autowired
    private CarYearlyCheckInfoMapper carYearlyCheckInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param yearlyCheckId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public CarYearlyCheckInfo selectCarYearlyCheckInfoById(Long yearlyCheckId)
    {
        return carYearlyCheckInfoMapper.selectCarYearlyCheckInfoById(yearlyCheckId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carYearlyCheckInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CarYearlyCheckInfo> selectCarYearlyCheckInfoList(CarYearlyCheckInfo carYearlyCheckInfo)
    {
        return carYearlyCheckInfoMapper.selectCarYearlyCheckInfoList(carYearlyCheckInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param carYearlyCheckInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCarYearlyCheckInfo(CarYearlyCheckInfo carYearlyCheckInfo)
    {
        carYearlyCheckInfo.setCreateTime(DateUtils.getNowDate());
        return carYearlyCheckInfoMapper.insertCarYearlyCheckInfo(carYearlyCheckInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param carYearlyCheckInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCarYearlyCheckInfo(CarYearlyCheckInfo carYearlyCheckInfo)
    {
        carYearlyCheckInfo.setUpdateTime(DateUtils.getNowDate());
        return carYearlyCheckInfoMapper.updateCarYearlyCheckInfo(carYearlyCheckInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param yearlyCheckIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarYearlyCheckInfoByIds(Long[] yearlyCheckIds)
    {
        return carYearlyCheckInfoMapper.deleteCarYearlyCheckInfoByIds(yearlyCheckIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param yearlyCheckId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarYearlyCheckInfoById(Long yearlyCheckId)
    {
        return carYearlyCheckInfoMapper.deleteCarYearlyCheckInfoById(yearlyCheckId);
    }
}
