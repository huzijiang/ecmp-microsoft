package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.DriverHeartbeatInfo;
import com.hq.ecmp.mscore.mapper.DriverHeartbeatInfoMapper;
import com.hq.ecmp.mscore.service.IDriverHeartbeatInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class DriverHeartbeatInfoServiceImpl implements IDriverHeartbeatInfoService
{
    @Autowired
    private DriverHeartbeatInfoMapper driverHeartbeatInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param heartId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public DriverHeartbeatInfo selectDriverHeartbeatInfoById(Long heartId)
    {
        return driverHeartbeatInfoMapper.selectDriverHeartbeatInfoById(heartId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param driverHeartbeatInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<DriverHeartbeatInfo> selectDriverHeartbeatInfoList(DriverHeartbeatInfo driverHeartbeatInfo)
    {
        return driverHeartbeatInfoMapper.selectDriverHeartbeatInfoList(driverHeartbeatInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param driverHeartbeatInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertDriverHeartbeatInfo(DriverHeartbeatInfo driverHeartbeatInfo)
    {
        driverHeartbeatInfo.setCreateTime(DateUtils.getNowDate());
        return driverHeartbeatInfoMapper.insertDriverHeartbeatInfo(driverHeartbeatInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param driverHeartbeatInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateDriverHeartbeatInfo(DriverHeartbeatInfo driverHeartbeatInfo)
    {
        driverHeartbeatInfo.setUpdateTime(DateUtils.getNowDate());
        return driverHeartbeatInfoMapper.updateDriverHeartbeatInfo(driverHeartbeatInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param heartIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteDriverHeartbeatInfoByIds(Long[] heartIds)
    {
        return driverHeartbeatInfoMapper.deleteDriverHeartbeatInfoByIds(heartIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param heartId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteDriverHeartbeatInfoById(Long heartId)
    {
        return driverHeartbeatInfoMapper.deleteDriverHeartbeatInfoById(heartId);
    }

    @Override
    public DriverHeartbeatInfo findNowLocation(Long driverId, Long orderId) {
        return driverHeartbeatInfoMapper.findNowLocation(driverId,orderId);
    }
}
