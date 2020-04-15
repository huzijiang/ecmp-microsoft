package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.RegimeTemplateInfo;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IRegimeTemplateInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param templateId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public RegimeTemplateInfo selectRegimeTemplateInfoById(Long templateId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param regimeTemplateInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<RegimeTemplateInfo> selectRegimeTemplateInfoList(RegimeTemplateInfo regimeTemplateInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param regimeTemplateInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertRegimeTemplateInfo(RegimeTemplateInfo regimeTemplateInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param regimeTemplateInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateRegimeTemplateInfo(RegimeTemplateInfo regimeTemplateInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param templateIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteRegimeTemplateInfoByIds(Long[] templateIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param templateId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteRegimeTemplateInfoById(Long templateId);
}
