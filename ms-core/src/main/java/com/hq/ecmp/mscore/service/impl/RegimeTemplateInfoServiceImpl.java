package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.RegimeTemplateInfo;
import com.hq.ecmp.mscore.mapper.RegimeTemplateInfoMapper;
import com.hq.ecmp.mscore.service.IRegimeTemplateInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class RegimeTemplateInfoServiceImpl implements IRegimeTemplateInfoService
{
    @Autowired
    private RegimeTemplateInfoMapper regimeTemplateInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param templateId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public RegimeTemplateInfo selectRegimeTemplateInfoById(Long templateId)
    {
        return regimeTemplateInfoMapper.selectRegimeTemplateInfoById(templateId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param regimeTemplateInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<RegimeTemplateInfo> selectRegimeTemplateInfoList(RegimeTemplateInfo regimeTemplateInfo)
    {
        return regimeTemplateInfoMapper.selectRegimeTemplateInfoList(regimeTemplateInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param regimeTemplateInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertRegimeTemplateInfo(RegimeTemplateInfo regimeTemplateInfo)
    {
        regimeTemplateInfo.setCreateTime(DateUtils.getNowDate());
        return regimeTemplateInfoMapper.insertRegimeTemplateInfo(regimeTemplateInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param regimeTemplateInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateRegimeTemplateInfo(RegimeTemplateInfo regimeTemplateInfo)
    {
        regimeTemplateInfo.setUpdateTime(DateUtils.getNowDate());
        return regimeTemplateInfoMapper.updateRegimeTemplateInfo(regimeTemplateInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param templateIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteRegimeTemplateInfoByIds(Long[] templateIds)
    {
        return regimeTemplateInfoMapper.deleteRegimeTemplateInfoByIds(templateIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param templateId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteRegimeTemplateInfoById(Long templateId)
    {
        return regimeTemplateInfoMapper.deleteRegimeTemplateInfoById(templateId);
    }
}
