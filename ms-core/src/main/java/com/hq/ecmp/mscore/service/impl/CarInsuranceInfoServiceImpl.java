package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.CarInsuranceInfo;
import com.hq.ecmp.mscore.mapper.CarInsuranceInfoMapper;
import com.hq.ecmp.mscore.service.ICarInsuranceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class CarInsuranceInfoServiceImpl implements ICarInsuranceInfoService
{
    @Autowired
    private CarInsuranceInfoMapper carInsuranceInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param carInsuranceId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public CarInsuranceInfo selectCarInsuranceInfoById(Long carInsuranceId)
    {
        return carInsuranceInfoMapper.selectCarInsuranceInfoById(carInsuranceId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carInsuranceInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CarInsuranceInfo> selectCarInsuranceInfoList(CarInsuranceInfo carInsuranceInfo)
    {
        return carInsuranceInfoMapper.selectCarInsuranceInfoList(carInsuranceInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param carInsuranceInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCarInsuranceInfo(CarInsuranceInfo carInsuranceInfo)
    {
        carInsuranceInfo.setCreateTime(DateUtils.getNowDate());
        return carInsuranceInfoMapper.insertCarInsuranceInfo(carInsuranceInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param carInsuranceInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCarInsuranceInfo(CarInsuranceInfo carInsuranceInfo)
    {
        carInsuranceInfo.setUpdateTime(DateUtils.getNowDate());
        return carInsuranceInfoMapper.updateCarInsuranceInfo(carInsuranceInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param carInsuranceIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarInsuranceInfoByIds(Long[] carInsuranceIds)
    {
        return carInsuranceInfoMapper.deleteCarInsuranceInfoByIds(carInsuranceIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param carInsuranceId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarInsuranceInfoById(Long carInsuranceId)
    {
        return carInsuranceInfoMapper.deleteCarInsuranceInfoById(carInsuranceId);
    }
}
