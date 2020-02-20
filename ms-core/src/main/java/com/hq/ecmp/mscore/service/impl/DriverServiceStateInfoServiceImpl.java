package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.DriverServiceStateInfo;
import com.hq.ecmp.mscore.mapper.DriverServiceStateInfoMapper;
import com.hq.ecmp.mscore.service.IDriverServiceStateInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class DriverServiceStateInfoServiceImpl implements IDriverServiceStateInfoService
{
    @Autowired
    private DriverServiceStateInfoMapper driverServiceStateInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public DriverServiceStateInfo selectDriverServiceStateInfoById(Long id)
    {
        return driverServiceStateInfoMapper.selectDriverServiceStateInfoById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param driverServiceStateInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<DriverServiceStateInfo> selectDriverServiceStateInfoList(DriverServiceStateInfo driverServiceStateInfo)
    {
        return driverServiceStateInfoMapper.selectDriverServiceStateInfoList(driverServiceStateInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param driverServiceStateInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertDriverServiceStateInfo(DriverServiceStateInfo driverServiceStateInfo)
    {
        driverServiceStateInfo.setCreateTime(DateUtils.getNowDate());
        return driverServiceStateInfoMapper.insertDriverServiceStateInfo(driverServiceStateInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param driverServiceStateInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateDriverServiceStateInfo(DriverServiceStateInfo driverServiceStateInfo)
    {
        driverServiceStateInfo.setUpdateTime(DateUtils.getNowDate());
        return driverServiceStateInfoMapper.updateDriverServiceStateInfo(driverServiceStateInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteDriverServiceStateInfoByIds(Long[] ids)
    {
        return driverServiceStateInfoMapper.deleteDriverServiceStateInfoByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteDriverServiceStateInfoById(Long id)
    {
        return driverServiceStateInfoMapper.deleteDriverServiceStateInfoById(id);
    }
}
