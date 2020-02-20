package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.SceneInfo;
import com.hq.ecmp.mscore.mapper.SceneInfoMapper;
import com.hq.ecmp.mscore.service.ISceneInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class SceneInfoServiceImpl implements ISceneInfoService
{
    @Autowired
    private SceneInfoMapper sceneInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param sceneId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public SceneInfo selectSceneInfoById(Long sceneId)
    {
        return sceneInfoMapper.selectSceneInfoById(sceneId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param sceneInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<SceneInfo> selectSceneInfoList(SceneInfo sceneInfo)
    {
        return sceneInfoMapper.selectSceneInfoList(sceneInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param sceneInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertSceneInfo(SceneInfo sceneInfo)
    {
        sceneInfo.setCreateTime(DateUtils.getNowDate());
        return sceneInfoMapper.insertSceneInfo(sceneInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param sceneInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateSceneInfo(SceneInfo sceneInfo)
    {
        sceneInfo.setUpdateTime(DateUtils.getNowDate());
        return sceneInfoMapper.updateSceneInfo(sceneInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param sceneIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteSceneInfoByIds(Long[] sceneIds)
    {
        return sceneInfoMapper.deleteSceneInfoByIds(sceneIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param sceneId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteSceneInfoById(Long sceneId)
    {
        return sceneInfoMapper.deleteSceneInfoById(sceneId);
    }
}
