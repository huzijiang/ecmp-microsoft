package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.ApplyInfo;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface ApplyInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param applyId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public ApplyInfo selectApplyInfoById(Long applyId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param applyInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<ApplyInfo> selectApplyInfoList(ApplyInfo applyInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param applyInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertApplyInfo(ApplyInfo applyInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param applyInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateApplyInfo(ApplyInfo applyInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param applyId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteApplyInfoById(Long applyId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param applyIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteApplyInfoByIds(Long[] applyIds);
}
