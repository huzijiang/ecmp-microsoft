package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.JourneyPassengerInfo;
import com.hq.ecmp.mscore.mapper.JourneyPassengerInfoMapper;
import com.hq.ecmp.mscore.service.IJourneyPassengerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class JourneyPassengerInfoServiceImpl implements IJourneyPassengerInfoService
{
    @Autowired
    private JourneyPassengerInfoMapper journeyPassengerInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param journeyPassengerId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public JourneyPassengerInfo selectJourneyPassengerInfoById(Long journeyPassengerId)
    {
        return journeyPassengerInfoMapper.selectJourneyPassengerInfoById(journeyPassengerId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param journeyPassengerInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<JourneyPassengerInfo> selectJourneyPassengerInfoList(JourneyPassengerInfo journeyPassengerInfo)
    {
        return journeyPassengerInfoMapper.selectJourneyPassengerInfoList(journeyPassengerInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param journeyPassengerInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertJourneyPassengerInfo(JourneyPassengerInfo journeyPassengerInfo)
    {
        journeyPassengerInfo.setCreateTime(DateUtils.getNowDate());
        return journeyPassengerInfoMapper.insertJourneyPassengerInfo(journeyPassengerInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param journeyPassengerInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateJourneyPassengerInfo(JourneyPassengerInfo journeyPassengerInfo)
    {
        journeyPassengerInfo.setUpdateTime(DateUtils.getNowDate());
        return journeyPassengerInfoMapper.updateJourneyPassengerInfo(journeyPassengerInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param journeyPassengerIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteJourneyPassengerInfoByIds(Long[] journeyPassengerIds)
    {
        return journeyPassengerInfoMapper.deleteJourneyPassengerInfoByIds(journeyPassengerIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param journeyPassengerId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteJourneyPassengerInfoById(Long journeyPassengerId)
    {
        return journeyPassengerInfoMapper.deleteJourneyPassengerInfoById(journeyPassengerId);
    }

    @Override
    public String getPeerPeople(Long journeyId) {
        return journeyPassengerInfoMapper.getPeerPeople(journeyId);
    }

	@Override
	public Integer queryPeerCount(Long journeyId) {
		// TODO Auto-generated method stub
		return null;
	}
}
