package com.hq.ecmp.mscore.service.impl;

import java.util.List;

import com.hq.ecmp.mscore.domain.SceneRegimeRelation;
import com.hq.ecmp.mscore.mapper.SceneRegimeRelationMapper;
import com.hq.ecmp.mscore.service.ISceneRegimeRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class SceneRegimeRelationServiceImpl implements ISceneRegimeRelationService
{
    @Autowired
    private SceneRegimeRelationMapper sceneRegimeRelationMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param sceneId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public SceneRegimeRelation selectSceneRegimeRelationById(Long sceneId)
    {
        return sceneRegimeRelationMapper.selectSceneRegimeRelationById(sceneId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param sceneRegimeRelation 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<SceneRegimeRelation> selectSceneRegimeRelationList(SceneRegimeRelation sceneRegimeRelation)
    {
        return sceneRegimeRelationMapper.selectSceneRegimeRelationList(sceneRegimeRelation);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param sceneRegimeRelation 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertSceneRegimeRelation(SceneRegimeRelation sceneRegimeRelation)
    {
        return sceneRegimeRelationMapper.insertSceneRegimeRelation(sceneRegimeRelation);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param sceneRegimeRelation 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateSceneRegimeRelation(SceneRegimeRelation sceneRegimeRelation)
    {
        return sceneRegimeRelationMapper.updateSceneRegimeRelation(sceneRegimeRelation);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param sceneIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteSceneRegimeRelationByIds(Long[] sceneIds)
    {
        return sceneRegimeRelationMapper.deleteSceneRegimeRelationByIds(sceneIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param sceneId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteSceneRegimeRelationById(Long sceneId)
    {
        return sceneRegimeRelationMapper.deleteSceneRegimeRelationById(sceneId);
    }
}
