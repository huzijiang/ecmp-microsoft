package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.RegimeInfo;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IRegimeInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param regimenId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public RegimeInfo selectRegimeInfoById(Long regimenId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param regimeInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<RegimeInfo> selectRegimeInfoList(RegimeInfo regimeInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param regimeInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertRegimeInfo(RegimeInfo regimeInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param regimeInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateRegimeInfo(RegimeInfo regimeInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param regimenIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteRegimeInfoByIds(Long[] regimenIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param regimenId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteRegimeInfoById(Long regimenId);
}
