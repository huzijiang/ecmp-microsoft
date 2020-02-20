package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.CarIllegalInfo;
import com.hq.ecmp.mscore.mapper.CarIllegalInfoMapper;
import com.hq.ecmp.mscore.service.ICarIllegalInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class CarIllegalInfoServiceImpl implements ICarIllegalInfoService
{
    @Autowired
    private CarIllegalInfoMapper carIllegalInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param illegalId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public CarIllegalInfo selectCarIllegalInfoById(Long illegalId)
    {
        return carIllegalInfoMapper.selectCarIllegalInfoById(illegalId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carIllegalInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CarIllegalInfo> selectCarIllegalInfoList(CarIllegalInfo carIllegalInfo)
    {
        return carIllegalInfoMapper.selectCarIllegalInfoList(carIllegalInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param carIllegalInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCarIllegalInfo(CarIllegalInfo carIllegalInfo)
    {
        carIllegalInfo.setCreateTime(DateUtils.getNowDate());
        return carIllegalInfoMapper.insertCarIllegalInfo(carIllegalInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param carIllegalInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCarIllegalInfo(CarIllegalInfo carIllegalInfo)
    {
        carIllegalInfo.setUpdateTime(DateUtils.getNowDate());
        return carIllegalInfoMapper.updateCarIllegalInfo(carIllegalInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param illegalIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarIllegalInfoByIds(Long[] illegalIds)
    {
        return carIllegalInfoMapper.deleteCarIllegalInfoByIds(illegalIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param illegalId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarIllegalInfoById(Long illegalId)
    {
        return carIllegalInfoMapper.deleteCarIllegalInfoById(illegalId);
    }
}
