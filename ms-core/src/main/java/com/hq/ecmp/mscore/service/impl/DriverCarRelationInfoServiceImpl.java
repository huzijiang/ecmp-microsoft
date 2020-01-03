package com.hq.ecmp.mscore.service.impl;

import java.util.List;

import com.hq.ecmp.mscore.domain.DriverCarRelationInfo;
import com.hq.ecmp.mscore.mapper.DriverCarRelationInfoMapper;
import com.hq.ecmp.mscore.service.IDriverCarRelationInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class DriverCarRelationInfoServiceImpl implements IDriverCarRelationInfoService
{
    @Autowired
    private DriverCarRelationInfoMapper driverCarRelationInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param userId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public DriverCarRelationInfo selectDriverCarRelationInfoById(Long userId)
    {
        return driverCarRelationInfoMapper.selectDriverCarRelationInfoById(userId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param driverCarRelationInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<DriverCarRelationInfo> selectDriverCarRelationInfoList(DriverCarRelationInfo driverCarRelationInfo)
    {
        return driverCarRelationInfoMapper.selectDriverCarRelationInfoList(driverCarRelationInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param driverCarRelationInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertDriverCarRelationInfo(DriverCarRelationInfo driverCarRelationInfo)
    {
        return driverCarRelationInfoMapper.insertDriverCarRelationInfo(driverCarRelationInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param driverCarRelationInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateDriverCarRelationInfo(DriverCarRelationInfo driverCarRelationInfo)
    {
        return driverCarRelationInfoMapper.updateDriverCarRelationInfo(driverCarRelationInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param userIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteDriverCarRelationInfoByIds(Long[] userIds)
    {
        return driverCarRelationInfoMapper.deleteDriverCarRelationInfoByIds(userIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param userId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteDriverCarRelationInfoById(Long userId)
    {
        return driverCarRelationInfoMapper.deleteDriverCarRelationInfoById(userId);
    }
}
