package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.CarGroupInfo;
import com.hq.ecmp.mscore.mapper.CarGroupInfoMapper;
import com.hq.ecmp.mscore.service.ICarGroupInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class CarGroupInfoServiceImpl implements ICarGroupInfoService
{
    @Autowired
    private CarGroupInfoMapper carGroupInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param carGroupId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public CarGroupInfo selectCarGroupInfoById(Long carGroupId)
    {
        return carGroupInfoMapper.selectCarGroupInfoById(carGroupId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carGroupInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CarGroupInfo> selectCarGroupInfoList(CarGroupInfo carGroupInfo)
    {
        return carGroupInfoMapper.selectCarGroupInfoList(carGroupInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param carGroupInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCarGroupInfo(CarGroupInfo carGroupInfo)
    {
        carGroupInfo.setCreateTime(DateUtils.getNowDate());
        return carGroupInfoMapper.insertCarGroupInfo(carGroupInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param carGroupInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCarGroupInfo(CarGroupInfo carGroupInfo)
    {
        carGroupInfo.setUpdateTime(DateUtils.getNowDate());
        return carGroupInfoMapper.updateCarGroupInfo(carGroupInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param carGroupIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarGroupInfoByIds(Long[] carGroupIds)
    {
        return carGroupInfoMapper.deleteCarGroupInfoByIds(carGroupIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param carGroupId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarGroupInfoById(Long carGroupId)
    {
        return carGroupInfoMapper.deleteCarGroupInfoById(carGroupId);
    }
}
