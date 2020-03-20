package com.hq.ecmp.mscore.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.hq.ecmp.mscore.vo.RegimenVO;
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
import com.hq.ecmp.mscore.domain.RegimeUseCarCityRuleInfo;
import com.hq.ecmp.mscore.domain.RegimeUseCarTimeRuleInfo;
import com.hq.ecmp.mscore.domain.RegimeVo;
import com.hq.ecmp.mscore.domain.SceneInfo;
import com.hq.ecmp.mscore.domain.SceneRegimeRelation;
import com.hq.ecmp.mscore.mapper.RegimeInfoMapper;
import com.hq.ecmp.mscore.mapper.SceneRegimeRelationMapper;
import com.hq.ecmp.mscore.mapper.UserRegimeRelationInfoMapper;
import com.hq.ecmp.mscore.service.IRegimeInfoService;
import com.hq.ecmp.mscore.service.IRegimeUseCarCityRuleInfoService;
import com.hq.ecmp.mscore.service.IRegimeUseCarTimeRuleInfoService;
import com.hq.ecmp.mscore.service.ISceneInfoService;

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
    @Autowired
    private IRegimeUseCarCityRuleInfoService regimeUseCarCityRuleInfoService;
    @Autowired
    private IRegimeUseCarTimeRuleInfoService regimeUseCarTimeRuleInfoService;
    @Autowired
    private ISceneInfoService sceneInfoService;
  

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
    public List<RegimenVO> findRegimeInfoListByUserId(Long userId, Long sceneId) {
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
        List<RegimenVO> regimeInfoList = regimeIds.stream().map(regimeId->regimeInfoMapper.selectRegimenVOById(regimeId)).collect(Collectors.toList());
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
		//生成制度
		Integer insertRegimeFlag = regimeInfoMapper.insertRegime(regimePo);
		Long regimenId=regimePo.getRegimenId();
		//生成城市限制记录
		List<String> cityLimitIds = regimePo.getCityLimitIds();
		if(null !=cityLimitIds && cityLimitIds.size()>0){
			List<RegimeUseCarCityRuleInfo> regimeUseCarCityRuleInfoList=new ArrayList<RegimeUseCarCityRuleInfo>(cityLimitIds.size());
			String ruleCity = regimePo.getRuleCity();
			String ruleAction=null;
			if(null !=ruleCity && "C002".equals(ruleCity) ){
				ruleAction="Y000";//在地点内的城市
			}else if(null !=ruleCity && "C003".equals(ruleCity)){
				ruleAction="N001";//在地点外的城市
			}
			for (String cityCode : cityLimitIds) {
				regimeUseCarCityRuleInfoList.add(new RegimeUseCarCityRuleInfo(regimenId, ruleAction, cityCode, regimePo.getOptId(), new Date()));
			}
			regimeUseCarCityRuleInfoService.batchInsert(regimeUseCarCityRuleInfoList);
		}
	
		//公务生成用车时间段限制记录
		List<RegimeUseCarTimeRuleInfo> regimeUseCarTimeRuleInfoList = regimePo.getRegimeUseCarTimeRuleInfoList();
		if(null !=regimeUseCarTimeRuleInfoList && regimeUseCarTimeRuleInfoList.size()>0){
			for (RegimeUseCarTimeRuleInfo regimeUseCarTimeRuleInfo : regimeUseCarTimeRuleInfoList) {
				regimeUseCarTimeRuleInfo.setRuleAction("Y000");//在限制时间段内
				regimeUseCarTimeRuleInfo.setRegimenId(regimenId);
				regimeUseCarTimeRuleInfo.setCreateBy(regimePo.getOptId());
				regimeUseCarTimeRuleInfo.setCreateTime(new Date());
			}
			regimeUseCarTimeRuleInfoService.batchInsert(regimeUseCarTimeRuleInfoList);
		}
		
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

	@Override
	public RegimeVo queryRegimeDetail(Long regimeId) {
		RegimeVo regimeVo = regimeInfoMapper.queryRegimeDetail(regimeId);
		if(null !=regimeVo){
			//查询对应的场景名称
			SceneInfo sceneInfo = sceneInfoService.querySceneByRegimeId(regimeId);
			if(null !=sceneInfo){
				regimeVo.setSceneName(sceneInfo.getName());
			}
			//查询制度使用人数
			Integer userCount = userRegimeRelationInfoMapper.queryRegimeUserCount(regimeId);
			regimeVo.setUseNum(userCount);
		}
		return regimeVo;
	}

	@Override
	public String queryUseCarModelByNoteId(Long noteId) {
		return regimeInfoMapper.queryUseCarModelByNoteId(noteId);
	}

	@Override
	public String queryUseCarModelByJourneyId(Long journeyId) {
		
		return null;
	}

	/**
	 * 获取用户可用网约车型
	 * @return
	 */
	@Override
	public String getUserOnlineCarLevels(Long regimenId) {
		return regimeInfoMapper.getUserOnlineCarLevels(regimenId);
	}
}
