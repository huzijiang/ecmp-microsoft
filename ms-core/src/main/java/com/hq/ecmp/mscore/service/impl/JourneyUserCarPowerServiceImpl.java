package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.JourneyUserCarPower;
import com.hq.ecmp.mscore.mapper.JourneyUserCarPowerMapper;
import com.hq.ecmp.mscore.service.IJourneyUserCarPowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class JourneyUserCarPowerServiceImpl implements IJourneyUserCarPowerService
{
    @Autowired
    private JourneyUserCarPowerMapper journeyUserCarPowerMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param powerId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public JourneyUserCarPower selectJourneyUserCarPowerById(Long powerId)
    {
        return journeyUserCarPowerMapper.selectJourneyUserCarPowerById(powerId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param journeyUserCarPower 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<JourneyUserCarPower> selectJourneyUserCarPowerList(JourneyUserCarPower journeyUserCarPower)
    {
        return journeyUserCarPowerMapper.selectJourneyUserCarPowerList(journeyUserCarPower);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param journeyUserCarPower 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertJourneyUserCarPower(JourneyUserCarPower journeyUserCarPower)
    {
        journeyUserCarPower.setCreateTime(DateUtils.getNowDate());
        return journeyUserCarPowerMapper.insertJourneyUserCarPower(journeyUserCarPower);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param journeyUserCarPower 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateJourneyUserCarPower(JourneyUserCarPower journeyUserCarPower)
    {
        journeyUserCarPower.setUpdateTime(DateUtils.getNowDate());
        return journeyUserCarPowerMapper.updateJourneyUserCarPower(journeyUserCarPower);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param powerIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteJourneyUserCarPowerByIds(Long[] powerIds)
    {
        return journeyUserCarPowerMapper.deleteJourneyUserCarPowerByIds(powerIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param powerId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteJourneyUserCarPowerById(Long powerId)
    {
        return journeyUserCarPowerMapper.deleteJourneyUserCarPowerById(powerId);
    }
}
