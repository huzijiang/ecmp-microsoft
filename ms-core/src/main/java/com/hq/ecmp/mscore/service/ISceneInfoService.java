package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.SceneInfo;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.dto.SceneDTO;
import com.hq.ecmp.mscore.dto.SceneSortDTO;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.ecmp.mscore.vo.SceneDetailVO;
import com.hq.ecmp.mscore.vo.SceneListVO;

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
    
    /**
     * 获取用户的用车场景
     * @param
     * @return
     */
    public List<SceneInfo> selectAllSceneSort(Long userId);

    /**
     * 新增用车场景
     * @param sceneDTO
     * @param userId
     * @return
     */
    void saveScene(SceneDTO sceneDTO, Long userId) throws Exception;

    /**
     * 修改用车场景
     * @param sceneDTO
     * @param userId
     * @return
     */
    void updateScene(SceneDTO sceneDTO, Long userId) throws Exception;

    /**
     * 查询场景详情
     * @param sceneDTO
     * @return
     */
    SceneDetailVO selectSceneDetail(SceneDTO sceneDTO);

    /**
     * 分页查询场景列表
     * @param pageRequest
     * @return
     */
    PageResult<SceneListVO> seleSceneByPage(PageRequest pageRequest);

    /**
     * 场景排序 上、下移
     * @param sceneSortDTO
     * @param userId
     */
    void sortScene(SceneSortDTO sceneSortDTO, Long userId);
    
    /**
     * 查询制度对应的场景名称
     * @param regimeId
     * @return
     */
    SceneInfo querySceneByRegimeId(Long regimeId);
}
