package com.hq.ecmp.mscore.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.RegimeInfo;
import com.hq.ecmp.mscore.domain.SceneInfo;
import com.hq.ecmp.mscore.domain.SceneRegimeRelation;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.dto.SceneDTO;
import com.hq.ecmp.mscore.dto.SceneSortDTO;
import com.hq.ecmp.mscore.mapper.RegimeInfoMapper;
import com.hq.ecmp.mscore.mapper.SceneInfoMapper;
import com.hq.ecmp.mscore.mapper.SceneRegimeRelationMapper;
import com.hq.ecmp.mscore.service.ISceneInfoService;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.ecmp.mscore.vo.SceneDetailVO;
import com.hq.ecmp.mscore.vo.SceneListVO;
import javafx.scene.Scene;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private SceneRegimeRelationMapper sceneRegimeRelationMapper;
    @Autowired
    private RegimeInfoMapper regimeInfoMapper;

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
     * 删除用车场景信息
     *
     * @param sceneId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteSceneInfoById(Long sceneId)
    {
        //查询用车场景下是否绑定用车制度
        List<Long> longs = sceneRegimeRelationMapper.selectRegimenIdsBySceneId(sceneId);
        //如果没绑定，则直接删除
        if(CollectionUtils.isEmpty(longs)){
            return sceneInfoMapper.deleteSceneInfoById(sceneId);
        }else {
            return 0;
        }
    }

	@Override
	public List<SceneInfo> selectAllSceneSort(Long userId) {
		return sceneInfoMapper.selectAllSceneSort(userId);
	}

    /**
     * 新增用车场景
     * @param sceneDTO
     * @param userId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveScene(SceneDTO sceneDTO, Long userId) throws Exception {
        SceneInfo sceneInfo = new SceneInfo();
        //图标名称
        sceneInfo.setIcon(sceneDTO.getIcon());
        //场景名称
        sceneInfo.setName(sceneDTO.getName());
        //创建人
        sceneInfo.setCreateBy(String.valueOf(userId));
        //创建时间
        sceneInfo.setCreateTime(new Date());
        //生效状态 0 立即生效  1 不生效 初始化为不生效
        sceneInfo.setEffectStatus("1");
        //1.新增用车场景表
        int i = sceneInfoMapper.insertSceneInfo(sceneInfo);
        if(i!=1){
            throw new Exception();
        }
        Long sceneId = sceneInfo.getSceneId();
        List<Long> regimenIds = sceneDTO.getRegimenIds();
        SceneRegimeRelation sceneRegimeRelation = null;
        for (Long regimenId : regimenIds) {
            sceneRegimeRelation = new SceneRegimeRelation();
            sceneRegimeRelation.setRegimenId(regimenId);
            sceneRegimeRelation.setSceneId(sceneId);
            //2.新增场景制度关系表
            int j = sceneRegimeRelationMapper.insertSceneRegimeRelation(sceneRegimeRelation);
            if(j!=1){
                throw new Exception();
            }
        }
    }

    /**
     * 修改用车场景
     * @param sceneDTO
     * @param userId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateScene(SceneDTO sceneDTO, Long userId) throws Exception {
        Long sceneId = sceneDTO.getSceneId();
        //得先解绑用车场景对应的用车制度 再删除用车场景
        //根据sceneId删除绑定的制度信息
        int i = sceneRegimeRelationMapper.deleteSceneRegimeRelationById(sceneId);
        //修改用车场景
        SceneInfo sceneInfo = new SceneInfo();
        sceneInfo.setName(sceneDTO.getName());
        sceneInfo.setIcon(sceneDTO.getIcon());
        sceneInfo.setSceneId(sceneId);
        sceneInfo.setUpdateBy(String.valueOf(userId));
        sceneInfo.setUpdateTime(new Date());
        int j = sceneInfoMapper.updateSceneInfo(sceneInfo);
        if(j != 1){
            throw new Exception();
        }
        //再绑定用车制度
        List<Long> regimenIds = sceneDTO.getRegimenIds();
        SceneRegimeRelation sceneRegimeRelation = null;
        for (Long regimenId : regimenIds) {
            sceneRegimeRelation = new SceneRegimeRelation();
            sceneRegimeRelation.setSceneId(sceneId);
            sceneRegimeRelation.setRegimenId(regimenId);
            sceneRegimeRelation.setUpdateBy(String.valueOf(userId));
            sceneRegimeRelation.setUpdateTime(new Date());
            int k = sceneRegimeRelationMapper.insertSceneRegimeRelation(sceneRegimeRelation);
            if(k !=1){
                throw new Exception();
            }
        }
    }

    /**
     * 查询场景详情
     * @param sceneDTO
     * @return
     */
    @Override
    public SceneDetailVO selectSceneDetail(SceneDTO sceneDTO) {
        SceneDetailVO sceneDetailVO = new SceneDetailVO();
        Long sceneId = sceneDTO.getSceneId();
        //根据sceneId查询场景信息
        SceneInfo sceneInfo = sceneInfoMapper.selectSceneInfoById(sceneId);
        sceneDetailVO.setIcon(sceneInfo.getIcon());
        sceneDetailVO.setName(sceneInfo.getIcon());
        Map<String, String> map = Maps.newHashMap();
        ArrayList<Map<String, String>> list = Lists.newArrayList();
        //根据sceneId查询对应的制度id集合
        List<Long> regimenIds = sceneRegimeRelationMapper.selectRegimenIdsBySceneId(sceneId);
        //根据制度id集合查询制度信息
        RegimeInfo regimeInfo = null;
        for (Long regimenId : regimenIds) {
            regimeInfo = regimeInfoMapper.selectRegimeInfoById(regimenId);
            String name = regimeInfo.getName();
            String approvalProcess = regimeInfo.getNeedApprovalProcess();
            map.put(name,approvalProcess);
            list.add(map);
        }
        sceneDetailVO.setRegimenInfos(list);
        return sceneDetailVO;
    }

    /**
     * 分页查询场景列表(带搜索功能)
     * @param pageRequest
     * @return
     */
    @Override
    public PageResult<SceneListVO> seleSceneByPage(PageRequest pageRequest) {
        //分页查询场景
        PageHelper.startPage(pageRequest.getPageNum(),pageRequest.getPageSize());
        List<SceneInfo> sceneInfos = sceneInfoMapper.selectAll(pageRequest.getSearch());
        //查询场景对应的用车制度
        ArrayList<SceneListVO> list = Lists.newArrayList();
        SceneListVO sceneListVO = null;
        for (SceneInfo sceneInfo : sceneInfos) {
            sceneListVO = SceneListVO.builder()
                    .name(sceneInfo.getName())
                    .sortNo(sceneInfo.getSortNo())
                    .icon(sceneInfo.getIcon())
                    .sceneId(sceneInfo.getSceneId())
                    .effectStatus(sceneInfo.getEffectStatus())
                    .build();
            //根据sceneId查询对应的制度id集合
            List<Long> regimenIds = sceneRegimeRelationMapper.selectRegimenIdsBySceneId(sceneInfo.getSceneId());
            //根据制度id集合查询制度名字集合
            List<String> regimenNames = regimenIds.stream().map(id -> regimeInfoMapper
                    .selectRegimeInfoById(id)).map(RegimeInfo::getName).collect(Collectors.toList());
            sceneListVO.setRegimenNames(regimenNames);
            list.add(sceneListVO);
        }
        //解析分页结果
        PageInfo<SceneInfo> info = new PageInfo<>(sceneInfos);
        return new PageResult<>(info.getTotal(),info.getPages(),list);
    }

    /**
     * 场景排序 上、下移
     * @param sceneSortDTO
     * @param userId
     */
    @Override
    public void sortScene(SceneSortDTO sceneSortDTO, Long userId) {
        //查询主场景
        SceneInfo mainSceneInfo = sceneInfoMapper.selectSceneInfoById(sceneSortDTO.getMainSceneId());
        //查询目标场景
        SceneInfo targetSceneInfo = sceneInfoMapper.selectSceneInfoById(sceneSortDTO.getTargetSceneId());
        //修改主场景
        mainSceneInfo.setCreateBy(String.valueOf(userId));
        mainSceneInfo.setSortNo(targetSceneInfo.getSortNo());
        mainSceneInfo.setCreateTime(new Date());
        sceneInfoMapper.updateSceneInfo(mainSceneInfo);
        //修改目标场景
        targetSceneInfo.setCreateTime(new Date());
        targetSceneInfo.setSortNo(mainSceneInfo.getSortNo());
        targetSceneInfo.setCreateBy(String.valueOf(userId));
        sceneInfoMapper.updateSceneInfo(targetSceneInfo);
    }

	@Override
	public SceneInfo querySceneByRegimeId(Long regimeId) {
		return sceneInfoMapper.querySceneByRegimeId(regimeId);
	}
}
