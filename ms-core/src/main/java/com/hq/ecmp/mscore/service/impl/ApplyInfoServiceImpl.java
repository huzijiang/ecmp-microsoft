package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.ApplyInfo;
import com.hq.ecmp.mscore.mapper.ApplyInfoMapper;
import com.hq.ecmp.mscore.service.IApplyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class ApplyInfoServiceImpl implements IApplyInfoService
{
    @Autowired
    private ApplyInfoMapper applyInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param applyId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public ApplyInfo selectApplyInfoById(Long applyId)
    {
        return applyInfoMapper.selectApplyInfoById(applyId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param applyInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<ApplyInfo> selectApplyInfoList(ApplyInfo applyInfo)
    {
        return applyInfoMapper.selectApplyInfoList(applyInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param applyInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertApplyInfo(ApplyInfo applyInfo)
    {
        applyInfo.setCreateTime(DateUtils.getNowDate());
        return applyInfoMapper.insertApplyInfo(applyInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param applyInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateApplyInfo(ApplyInfo applyInfo)
    {
        applyInfo.setUpdateTime(DateUtils.getNowDate());
        return applyInfoMapper.updateApplyInfo(applyInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param applyIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteApplyInfoByIds(Long[] applyIds)
    {
        return applyInfoMapper.deleteApplyInfoByIds(applyIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param applyId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteApplyInfoById(Long applyId)
    {
        return applyInfoMapper.deleteApplyInfoById(applyId);
    }
}
