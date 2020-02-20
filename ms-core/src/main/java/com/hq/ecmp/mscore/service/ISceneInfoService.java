package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.SceneInfo;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface ISceneInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param sceneId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public SceneInfo selectSceneInfoById(Long sceneId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param sceneInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<SceneInfo> selectSceneInfoList(SceneInfo sceneInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param sceneInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertSceneInfo(SceneInfo sceneInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param sceneInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateSceneInfo(SceneInfo sceneInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param sceneIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteSceneInfoByIds(Long[] sceneIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param sceneId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteSceneInfoById(Long sceneId);
}
