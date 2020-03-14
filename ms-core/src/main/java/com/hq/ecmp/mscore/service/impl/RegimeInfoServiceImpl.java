package com.hq.ecmp.mscore.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.util.StringUtil;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.RegimeInfo;
import com.hq.ecmp.mscore.domain.RegimeOpt;
import com.hq.ecmp.mscore.domain.RegimePo;
import com.hq.ecmp.mscore.domain.RegimeQueryPo;
import com.hq.ecmp.mscore.domain.RegimeVo;
import com.hq.ecmp.mscore.domain.SceneRegimeRelation;
import com.hq.ecmp.mscore.mapper.RegimeInfoMapper;
import com.hq.ecmp.mscore.mapper.SceneRegimeRelationMapper;
import com.hq.ecmp.mscore.mapper.UserRegimeRelationInfoMapper;
import com.hq.ecmp.mscore.service.IRegimeInfoService;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class RegimeInfoServiceImpl implements IRegimeInfoService {

    @Autowired
    private UserRegimeRelationInfoMapper userRegimeRelationInfoMapper;
    @Autowired
    private RegimeInfoMapper regimeInfoMapper;
    @Autowired
    private SceneRegimeRelationMapper sceneRegimeRelationMapper;
  

    /**
     * 根据用车制度id查询用车值得详细信息
     *
     * @param regimenId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public RegimeInfo selectRegimeInfoById(Long regimenId) {
        return regimeInfoMapper.selectRegimeInfoById(regimenId);
    }

    /**
     * 查询所有用车制度信息
     * @return
     */
    @Override
    public List<RegimeInfo> selectAll() {
        return regimeInfoMapper.selectAll();
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param regimeInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<RegimeInfo> selectRegimeInfoList(RegimeInfo regimeInfo) {
        return regimeInfoMapper.selectRegimeInfoList(regimeInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param regimeInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertRegimeInfo(RegimeInfo regimeInfo) {
        regimeInfo.setCreateTime(DateUtils.getNowDate());
        return regimeInfoMapper.insertRegimeInfo(regimeInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param regimeInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateRegimeInfo(RegimeInfo regimeInfo) {
        regimeInfo.setUpdateTime(DateUtils.getNowDate());
        return regimeInfoMapper.updateRegimeInfo(regimeInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param regimenIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteRegimeInfoByIds(Long[] regimenIds) {
        return regimeInfoMapper.deleteRegimeInfoByIds(regimenIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param regimenId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteRegimeInfoById(Long regimenId) {
        return regimeInfoMapper.deleteRegimeInfoById(regimenId);
    }

    /**
     * 根据用户id查询用车制度集合(有场景id,则加场景条件)
     *
     * @param userId
     * @return
     */
    @Override
    public List<RegimeInfo> findRegimeInfoListByUserId(Long userId,Long sceneId) {
        //根据userId查询regimeId集合
        List<Long> regimeIds = userRegimeRelationInfoMapper.selectIdsByUserId(userId);
        //如果有制度条件限制,则进行条件筛选
        if(sceneId != null){
            //根据sceneId查询制度id集合
            List<Long> regimenIds2 = sceneRegimeRelationMapper.selectRegimenIdsBySceneId(sceneId);
            //求交集
            regimeIds.retainAll(regimenIds2);
        }
        //根据regimeId集合查询RegimeInfo集合
        List<RegimeInfo> regimeInfoList = regimeIds.stream().map(regimeId->regimeInfoMapper.selectRegimeInfoById(regimeId)).collect(Collectors.toList());
        return regimeInfoList;
    }

	@Override
	public boolean findOwnCar(Long regimenId) {
		RegimeInfo regimeInfo = selectRegimeInfoById(regimenId);
		String canUseCarMode = regimeInfo.getCanUseCarMode();
		if(StringUtil.isEmpty(canUseCarMode)){
			return false;
		}
		List<String> list = Arrays.asList(canUseCarMode.split(","));
		if(list.contains("W001")){
			return true;
		}else{
			return false;
		}
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean createRegime(RegimePo regimePo) {
		//生成制度  TODO
		Long regimenId=null;
		//
		List<Long> userList = regimePo.getUserList();
		if(null !=userList && userList.size()>0){
			//生成可用用户-制度中间记录
			userRegimeRelationInfoMapper.batchInsertUser(regimenId, userList);
		}
		Long sceneId = regimePo.getSceneId();
		if(null !=sceneId){
			//生成制度-场景记录
			SceneRegimeRelation sceneRegimeRelation = new SceneRegimeRelation();
			sceneRegimeRelation.setSceneId(sceneId);
			sceneRegimeRelation.setRegimenId(regimenId);
			sceneRegimeRelationMapper.insertSceneRegimeRelation(sceneRegimeRelation);
			
		}
		return true;
	}

	@Override
	public List<RegimeVo> queryRegimeList(RegimeQueryPo regimeQueryPo) {
		List<RegimeVo> regimeList = regimeInfoMapper.queryRegimeList(regimeQueryPo);
		if(null !=regimeList && regimeList.size()>0){
			for (RegimeVo regimeVo : regimeList) {
				//查询该制度的使用人数
				Integer userCount = userRegimeRelationInfoMapper.queryRegimeUserCount(regimeVo.getRegimeId());
				regimeVo.setUseNum(userCount);
			}
		}
		return regimeList;
	}

	@Override
	public Integer queryRegimeListCount(RegimeQueryPo regimeQueryPo) {
		return regimeInfoMapper.queryRegimeListCount(regimeQueryPo);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean optRegime(RegimeOpt regimeOpt) {
		Integer optType = regimeOpt.getOptType();
		if(optType==1 || optType==0){
			regimeInfoMapper.updateStatus(regimeOpt);
		}else{
			//物理删除
			regimeInfoMapper.deleteRegimeInfoById(regimeOpt.getRegimeId());
			userRegimeRelationInfoMapper.deleteUserRegimeRelationInfoByRegimeId(regimeOpt.getRegimeId());
			sceneRegimeRelationMapper.deleteSceneRegimeRelationByRegimeId(regimeOpt.getRegimeId());
		}
		return true;
	}

	@Override
	public RegimeInfo queryRegimeType(Long regimeId) {
		return regimeInfoMapper.queryRegimeType(regimeId);
	}
}
