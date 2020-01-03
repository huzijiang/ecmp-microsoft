package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.CarInfo;
import com.hq.ecmp.mscore.mapper.CarInfoMapper;
import com.hq.ecmp.mscore.service.ICarInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class CarInfoServiceImpl implements ICarInfoService
{
    @Autowired
    private CarInfoMapper carInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param carId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public CarInfo selectCarInfoById(Long carId)
    {
        return carInfoMapper.selectCarInfoById(carId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CarInfo> selectCarInfoList(CarInfo carInfo)
    {
        return carInfoMapper.selectCarInfoList(carInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param carInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCarInfo(CarInfo carInfo)
    {
        carInfo.setCreateTime(DateUtils.getNowDate());
        return carInfoMapper.insertCarInfo(carInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param carInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCarInfo(CarInfo carInfo)
    {
        carInfo.setUpdateTime(DateUtils.getNowDate());
        return carInfoMapper.updateCarInfo(carInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param carIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarInfoByIds(Long[] carIds)
    {
        return carInfoMapper.deleteCarInfoByIds(carIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param carId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarInfoById(Long carId)
    {
        return carInfoMapper.deleteCarInfoById(carId);
    }
}
