package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.EcmpQuestionnaire;
import com.hq.ecmp.mscore.mapper.EcmpQuestionnaireMapper;
import com.hq.ecmp.mscore.service.IEcmpQuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author hqer
 * @date 2020-06-11
 */
@Service
public class EcmpQuestionnaireServiceImpl implements IEcmpQuestionnaireService
{
    @Autowired
    private EcmpQuestionnaireMapper ecmpQuestionnaireMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public EcmpQuestionnaire selectEcmpQuestionnaireById(Long id)
    {
        return ecmpQuestionnaireMapper.selectEcmpQuestionnaireById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param ecmpQuestionnaire 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<EcmpQuestionnaire> selectEcmpQuestionnaireList(EcmpQuestionnaire ecmpQuestionnaire)
    {
        return ecmpQuestionnaireMapper.selectEcmpQuestionnaireList(ecmpQuestionnaire);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param ecmpQuestionnaire 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertEcmpQuestionnaire(EcmpQuestionnaire ecmpQuestionnaire)
    {
        ecmpQuestionnaire.setCreateTime(DateUtils.getNowDate());
        return ecmpQuestionnaireMapper.insertEcmpQuestionnaire(ecmpQuestionnaire);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param ecmpQuestionnaire 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateEcmpQuestionnaire(EcmpQuestionnaire ecmpQuestionnaire)
    {
        ecmpQuestionnaire.setUpdateTime(DateUtils.getNowDate());
        return ecmpQuestionnaireMapper.updateEcmpQuestionnaire(ecmpQuestionnaire);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteEcmpQuestionnaireByIds(Long[] ids)
    {
        return ecmpQuestionnaireMapper.deleteEcmpQuestionnaireByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteEcmpQuestionnaireById(Long id)
    {
        return ecmpQuestionnaireMapper.deleteEcmpQuestionnaireById(id);
    }
}