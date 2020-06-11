package com.hq.ecmp.mscore.service;


import com.hq.ecmp.mscore.domain.EcmpQuestionnaire;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author hqer
 * @date 2020-06-11
 */
public interface IEcmpQuestionnaireService 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public EcmpQuestionnaire selectEcmpQuestionnaireById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param ecmpQuestionnaire 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<EcmpQuestionnaire> selectEcmpQuestionnaireList(EcmpQuestionnaire ecmpQuestionnaire);

    /**
     * 新增【请填写功能名称】
     * 
     * @param ecmpQuestionnaire 【请填写功能名称】
     * @return 结果
     */
    public int insertEcmpQuestionnaire(EcmpQuestionnaire ecmpQuestionnaire);

    /**
     * 修改【请填写功能名称】
     * 
     * @param ecmpQuestionnaire 【请填写功能名称】
     * @return 结果
     */
    public int updateEcmpQuestionnaire(EcmpQuestionnaire ecmpQuestionnaire);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteEcmpQuestionnaireByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteEcmpQuestionnaireById(Long id);
}