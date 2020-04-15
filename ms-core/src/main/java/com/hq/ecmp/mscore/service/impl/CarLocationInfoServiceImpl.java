package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.CarLocationInfo;
import com.hq.ecmp.mscore.mapper.CarLocationInfoMapper;
import com.hq.ecmp.mscore.service.ICarLocationInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class CarLocationInfoServiceImpl implements ICarLocationInfoService
{
    @Autowired
    private CarLocationInfoMapper carLocationInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param carLocationId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public CarLocationInfo selectCarLocationInfoById(String carLocationId)
    {
        return carLocationInfoMapper.selectCarLocationInfoById(carLocationId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carLocationInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CarLocationInfo> selectCarLocationInfoList(CarLocationInfo carLocationInfo)
    {
        return carLocationInfoMapper.selectCarLocationInfoList(carLocationInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param carLocationInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCarLocationInfo(CarLocationInfo carLocationInfo)
    {
        carLocationInfo.setCreateTime(DateUtils.getNowDate());
        return carLocationInfoMapper.insertCarLocationInfo(carLocationInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param carLocationInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCarLocationInfo(CarLocationInfo carLocationInfo)
    {
        carLocationInfo.setUpdateTime(DateUtils.getNowDate());
        return carLocationInfoMapper.updateCarLocationInfo(carLocationInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param carLocationIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarLocationInfoByIds(String[] carLocationIds)
    {
        return carLocationInfoMapper.deleteCarLocationInfoByIds(carLocationIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param carLocationId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarLocationInfoById(String carLocationId)
    {
        return carLocationInfoMapper.deleteCarLocationInfoById(carLocationId);
    }
}
