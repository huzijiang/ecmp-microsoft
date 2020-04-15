package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.SceneInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface SceneInfoMapper
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
     * 删除【请填写功能名称】
     *
     * @param sceneId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteSceneInfoById(Long sceneId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param sceneIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSceneInfoByIds(Long[] sceneIds);
    
    
	public List<SceneInfo> selectAllSceneSort(Long userId);

    /**
     * 查询所有场景（带搜索功能）
     * @return
     */
    List<SceneInfo> selectAll(@Param("name") String name);
    
    public SceneInfo querySceneByRegimeId(Long regimeId);
}
