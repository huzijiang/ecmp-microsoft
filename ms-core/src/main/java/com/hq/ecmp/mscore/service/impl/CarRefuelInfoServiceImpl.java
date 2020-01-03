package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.CarRefuelInfo;
import com.hq.ecmp.mscore.mapper.CarRefuelInfoMapper;
import com.hq.ecmp.mscore.service.ICarRefuelInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class CarRefuelInfoServiceImpl implements ICarRefuelInfoService
{
    @Autowired
    private CarRefuelInfoMapper carRefuelInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param refuelId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public CarRefuelInfo selectCarRefuelInfoById(Long refuelId)
    {
        return carRefuelInfoMapper.selectCarRefuelInfoById(refuelId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carRefuelInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CarRefuelInfo> selectCarRefuelInfoList(CarRefuelInfo carRefuelInfo)
    {
        return carRefuelInfoMapper.selectCarRefuelInfoList(carRefuelInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param carRefuelInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCarRefuelInfo(CarRefuelInfo carRefuelInfo)
    {
        carRefuelInfo.setCreateTime(DateUtils.getNowDate());
        return carRefuelInfoMapper.insertCarRefuelInfo(carRefuelInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param carRefuelInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCarRefuelInfo(CarRefuelInfo carRefuelInfo)
    {
        carRefuelInfo.setUpdateTime(DateUtils.getNowDate());
        return carRefuelInfoMapper.updateCarRefuelInfo(carRefuelInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param refuelIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarRefuelInfoByIds(Long[] refuelIds)
    {
        return carRefuelInfoMapper.deleteCarRefuelInfoByIds(refuelIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param refuelId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarRefuelInfoById(Long refuelId)
    {
        return carRefuelInfoMapper.deleteCarRefuelInfoById(refuelId);
    }
}
