package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.SceneRegimeRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface SceneRegimeRelationMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param sceneId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public SceneRegimeRelation selectSceneRegimeRelationById(Long sceneId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param sceneRegimeRelation 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<SceneRegimeRelation> selectSceneRegimeRelationList(SceneRegimeRelation sceneRegimeRelation);

    /**
     * 新增【请填写功能名称】
     *
     * @param sceneRegimeRelation 【请填写功能名称】
     * @return 结果
     */
    public int insertSceneRegimeRelation(SceneRegimeRelation sceneRegimeRelation);

    /**
     * 修改【请填写功能名称】
     *
     * @param sceneRegimeRelation 【请填写功能名称】
     * @return 结果
     */
    public int updateSceneRegimeRelation(SceneRegimeRelation sceneRegimeRelation);

    /**
     * 删除【请填写功能名称】
     *
     * @param sceneId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteSceneRegimeRelationById(Long sceneId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param sceneIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSceneRegimeRelationByIds(Long[] sceneIds);

    /**
     * 根据场景id查询制度id集合
     * @param sceneId
     * @return
     */
    List<Long> selectRegimenIdsBySceneId(@Param("sceneId") Long sceneId );
    
    public Integer deleteSceneRegimeRelationByRegimeId(Long regimeId);

}
