package com.hq.ecmp.mscore.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import com.hq.core.security.LoginUser;
import com.hq.ecmp.constant.*;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.RegimeCheckDto;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.mscore.vo.*;
import com.hq.ecmp.util.DateFormatUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.Now;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.util.StringUtil;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.vo.RegimenVO;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

import static com.hq.ecmp.constant.CommonConstant.ALLOW_DATA;
import static com.hq.ecmp.util.DateFormatUtils.DATE_TIME_FORMAT;
import static com.hq.ecmp.util.DateFormatUtils.TIME_FORMAT;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
@Slf4j
public class RegimeInfoServiceImpl implements IRegimeInfoService {

    @Resource
    private UserRegimeRelationInfoMapper userRegimeRelationInfoMapper;
    @Autowired
    private RegimeInfoMapper regimeInfoMapper;
    @Resource
    private SceneRegimeRelationMapper sceneRegimeRelationMapper;
    @Autowired
    private RegimeUseCarCityRuleInfoMapper regimeUseCarCityRuleInfoMapper;
    @Autowired
    private ISceneInfoService sceneInfoService;
    @Autowired
    private CarGroupServeScopeInfoMapper carGroupServeScopeInfoMapper;
    @Autowired
    private CarGroupInfoMapper carGroupInfoMapper;
    @Autowired
	private ApproveTemplateNodeInfoMapper approveTemplateNodeInfoMapper;
    @Resource
	private ThirdService thirdService;
    @Resource
	private OrderAddressInfoMapper orderAddressInfoMapper;
    @Resource
	private OrderInfoMapper orderInfoMapper;
    @Autowired
    private ApplyInfoMapper applyInfoMapper;
    @Autowired
    private IEcmpOrgService orgService;
    @Autowired
    private ChinaCityMapper chinaCityMapper;
    @Resource
	private RegimeUseCarTimeRuleInfoMapper useCarTimeRuleInfoMapper;
    @Resource
	private CloudWorkDateInfoMapper workDateInfoMapper;


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
    public List<RegimenVO> findRegimeInfoListByUserId(Long userId, Long sceneId) throws Exception{
        //根据userId查询有效的regimeId集合
		List<Long> regimeIds = regimeInfoMapper.selectEnableRegimenIdByUserId(userId);
        //如果有制度条件限制,则进行条件筛选
        if(sceneId != null){
            //根据sceneId查询制度id集合
            List<Long> regimenIds2 = sceneRegimeRelationMapper.selectRegimenIdsBySceneId(sceneId);
            //求交集
            regimeIds.retainAll(regimenIds2);
        }
        //根据regimeId集合查询RegimeInfo集合
		List<RegimenVO> regimeInfoList = new ArrayList<>();
		for (Long regimeId : regimeIds) {
			RegimenVO regimenVO = regimeInfoMapper.selectRegimenVOById(regimeId);
			//查询制度对应的审批第一个节点类型
			if(regimenVO != null) {
				//过滤掉已经过期制度
				String allowDate = regimenVO.getAllowDate();
				if(allowDate != null && !ALLOW_DATA.equals(allowDate)){
					String allowEndDate = allowDate.split("-")[1];
					DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
					Date parse = dateFormat.parse(allowEndDate);
					Date date = new Date();
					if( date.getTime() > parse.getTime()){
						continue;
					}
				}
				ApproveTemplateNodeInfo approveTemplateNodeInfo = approveTemplateNodeInfoMapper.selectFirstOpproveNode(regimeId);
				if (ObjectUtils.isNotEmpty(approveTemplateNodeInfo)) {
					String approverType = approveTemplateNodeInfo.getApproverType();
					regimenVO.setFirstOpproveNodeTypeIsProjectLeader(ApproveTypeEnum.APPROVE_T004.getKey().equals(approverType) ? true : false);
				}else{
					regimenVO.setFirstOpproveNodeTypeIsProjectLeader(false);
				}
				regimeInfoList.add(regimenVO);
			}
		}
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
		if(list.contains(CarConstant.USR_CARD_MODE_HAVE)){
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
			regimeUseCarCityRuleInfoMapper.batchInsert(regimeUseCarCityRuleInfoList);
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
			useCarTimeRuleInfoMapper.batchInsert(regimeUseCarTimeRuleInfoList);
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
				//查询是用来改制度的申请单数量
				regimeVo.setApplyUseNum(applyInfoMapper.queryApplyNumByRegimeId(regimeVo.getRegimeId()));
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
		String optType = regimeOpt.getOptType();
		if("Y000".equals(optType) || "E000".equals(optType)){
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
				regimeVo.setSceneId(sceneInfo.getSceneId());
			}
			//查询制度使用人数
			Integer userCount = userRegimeRelationInfoMapper.queryRegimeUserCount(regimeId);
			regimeVo.setUseNum(userCount);
			//查询使用该制度的用户
			regimeVo.setUserList(userRegimeRelationInfoMapper.queryRegimeUser(regimeId));
			//如果是公务用车制度  则查询用车时段限制和用车城市限制
			if(CarConstant.USE_CAR_TYPE_OFFICIAL.equals(regimeVo.getRegimenType())){
				String ruleCity = regimeVo.getRuleCity();
				if("C002".equals(ruleCity)){
					List<String> queryLimitCityCodeList = regimeUseCarCityRuleInfoMapper.queryLimitCityCodeList(regimeId);
					regimeVo.setCityLimitIds(queryLimitCityCodeList);
				}
				if("C003".equals(ruleCity)){
					List<String> queryLimitCityCodeList = regimeUseCarCityRuleInfoMapper.queryLimitCityCodeList(regimeId);
					regimeVo.setNotCityLimitIds(queryLimitCityCodeList);
				}

				if(!"T001".equals(regimeVo.getRuleTime())){
					List<RegimeUseCarTimeRuleInfo> queryRegimeUseCarTimeRuleInfoList = useCarTimeRuleInfoMapper.queryRegimeUseCarTimeRuleInfoList(regimeId,null);
					regimeVo.setRegimeUseCarTimeRuleInfoList(queryRegimeUseCarTimeRuleInfoList);
				}
			}
		}
		return regimeVo;
	}

	@Override
	public RegimeInfo queryUseCarModelByNoteId(Long noteId) {
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
	public String getUserOnlineCarLevels(Long regimenId,String type) {
		RegimenVO regimenVO = regimeInfoMapper.selectRegimenVOById(regimenId);
		if(ObjectUtils.isNotEmpty(regimenVO)){
			String regimenType = regimenVO.getRegimenType();
			if(ApplyTypeEnum.APPLY_BUSINESS_TYPE.getKey().equals(regimenType)){
				return regimenVO.getUseCarModeOnlineLevel();
			}else if(ApplyTypeEnum.APPLY_TRAVEL_TYPE.getKey().equals(regimenType)){
				//网约车 需判断   C001  接机   C009  送机    C222  市内用车
				if(CarConstant.CITY_USE_CAR.equals(type)){
					//如果是差旅市内用车（预约）
					return regimenVO.getTravelUseCarModeOnlineLevel();
				}else {
					//如果是接送机
					return regimenVO.getAsUseCarModeOnlineLevel();
				}
			}
		}
		return null;
	}

	@Override
	public boolean judgeNotDispatch(Long regimeId, String cityCode) {
		if(StringUtil.isEmpty(cityCode)){
			return false;
		}
		RegimeInfo regimeInfo = regimeInfoMapper.selectRegimeInfoById(regimeId);
		if(null==regimeInfo){
			return false;
		}
		String canUseCarMode = regimeInfo.getCanUseCarMode();
		if(StringUtil.isEmpty(canUseCarMode)){
			return false;
		}
		String[] split = canUseCarMode.split(",");
		List<String> asList = Arrays.asList(split);
		if(asList.size()==1){
			if(CarConstant.USR_CARD_MODE_HAVE.equals(asList.get(0))){
				return false;
			}else{
				return true;
			}
		}
		//自有车+网约车时，但上车地点“不在”车队的用车城市 范围内
		List<Long> result = carGroupServeScopeInfoMapper.queryCarGroupByCity(cityCode);
		if(null !=result && result.size()>0){
			return false;
		}
		return true;
	}

	/**
	 * app端查询用车制度详情
	 * @param regimenId
	 * @return
	 */
	@Override
	public RegimeVo selectRegimeDetailById(Long regimenId) {
		RegimeVo regimeVo = regimeInfoMapper.queryRegimeDetail(regimenId);
		if(ObjectUtils.isEmpty(regimenId)){
			throw new RuntimeException("查询制度详情失败");
		}
		return regimeVo;
	}

	@Override
	public String queryCarModeLevel(Long orderId, String useCarMode) {
		log.info("查询订单【"+orderId+"】车型接口开始:用车方式:"+useCarMode);
		String carModeLevel=null;
		RegimeVo regimeVo = regimeInfoMapper.queryRegimeInfoByOrderId(orderId);
		String 	serviceType= regimeVo.getServiceType();
		String regimenType = regimeVo.getRegimenType();
		if (StringUtil.isNotEmpty(useCarMode)) {
			// 传入了用车方式
			if (CarConstant.USR_CARD_MODE_NET.equals(useCarMode)) {
				// 用车方式-网约车
				if (CarConstant.USE_CAR_TYPE_OFFICIAL.equals(regimenType)) {
					// 公务
					carModeLevel = regimeVo.getUseCarModeOnlineLevel();
				} else {
					// 差旅
					if(OrderServiceType.getSendAndPick().contains(serviceType)){
						//接送机
						carModeLevel = regimeVo.getAsUseCarModeOnlineLevel();
					}
					
					if(OrderServiceType.ORDER_SERVICE_TYPE_APPOINTMENT.getBcState().equals(serviceType)){
						//城市用车
						carModeLevel=regimeVo.getTravelUseCarModeOnlineLevel();
					}
					
				}
			} else {
				// 用车方式-自有车
				if (CarConstant.USE_CAR_TYPE_OFFICIAL.equals(regimenType)) {
					carModeLevel = regimeVo.getUseCarModeOwnerLevel();
				} else {
					// 差旅
					if(OrderServiceType.getSendAndPick().contains(serviceType)){
						//接送机
						carModeLevel = regimeVo.getAsUseCarModeOwnerLevel();
					}
					
					if(OrderServiceType.ORDER_SERVICE_TYPE_APPOINTMENT.getBcState().equals(serviceType)){
						//城市用车
						carModeLevel=regimeVo.getTravelUseCarModeOwnerLevel();
					}
					
				}
			}
		} else {
			// 没有传用车方式 则默认取网约车的车型配置
			if (CarConstant.USE_CAR_TYPE_OFFICIAL.equals(regimenType)) {
				carModeLevel = regimeVo.getUseCarModeOnlineLevel();
			} else {
				// 差旅
				if(OrderServiceType.getSendAndPick().contains(serviceType)){
					//接送机
					carModeLevel = regimeVo.getAsUseCarModeOnlineLevel();
				}
				
				if(OrderServiceType.ORDER_SERVICE_TYPE_APPOINTMENT.getBcState().equals(serviceType)){
					//城市用车
					carModeLevel=regimeVo.getTravelUseCarModeOnlineLevel();
				}
			}
		}
		log.info("查询订单【"+orderId+"】车型结果:"+carModeLevel);
		return carModeLevel;
	}

	/**
	 * 通过订单，用车方式获取车型以及预估价格等相关信息
	 * @param orderId
	 * @param useCarMode,不传默认为网约车 W100-自有车 	W200-网约车
	 */
	@Override
	public List<CarLevelAndPriceReVo> getCarlevelAndPriceByOrderId(Long orderId, String useCarMode) throws Exception {
		Date bookingStartTime = null;
		String groupIds = queryCarModeLevel(orderId, useCarMode);
		OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
		EstimatePriceVo estimatePriceVo = new EstimatePriceVo();
		estimatePriceVo.setServiceType(Integer.parseInt(orderInfo.getServiceType()));
		estimatePriceVo.setGroups(groupIds);
		OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
		orderAddressInfo.setOrderId(orderId);
		List<OrderAddressInfo> orderAddressInfos = orderAddressInfoMapper.selectOrderAddressInfoList(orderAddressInfo);
		for (OrderAddressInfo orderAddressInfoCh:
			 orderAddressInfos) {
			if(orderAddressInfoCh.getType().equals(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT)){
				estimatePriceVo.setCityId(orderAddressInfoCh.getCityPostalCode());
				estimatePriceVo.setBookingDate((orderAddressInfoCh.getActionTime().getTime()+"").substring(0,10));
				bookingStartTime = orderAddressInfoCh.getActionTime();
				estimatePriceVo.setBookingStartAddr(orderAddressInfoCh.getAddress());
				estimatePriceVo.setBookingStartPointLo(orderAddressInfoCh.getLongitude()+"");
				estimatePriceVo.setBookingStartPointLa(orderAddressInfoCh.getLatitude()+"");
			}else if(orderAddressInfoCh.getType().equals(OrderConstant.ORDER_ADDRESS_ACTUAL_ARRIVE)){
				estimatePriceVo.setBookingEndAddr(orderAddressInfoCh.getAddress());
				estimatePriceVo.setBookingEndPointLo(orderAddressInfoCh.getLongitude()+"");
				estimatePriceVo.setBookingEndPointLa(orderAddressInfoCh.getLatitude()+"");
			}
		}
		List<CarCostVO> carCostVOS = thirdService.enterpriseOrderGetCalculatePrice(estimatePriceVo);
		List<CarLevelAndPriceReVo> result = new ArrayList<>();
		for (int i = 0; i < carCostVOS.size() ; i++) {
			CarCostVO carCostVO = carCostVOS.get(i);
			CarLevelAndPriceReVo carLevelAndPriceReVo = new CarLevelAndPriceReVo();
			carLevelAndPriceReVo.setDuration(Integer.parseInt(carCostVO.getDuration()));
			carLevelAndPriceReVo.setOnlineCarLevel(carCostVO.getGroupId());
			carLevelAndPriceReVo.setEstimatePrice(carCostVO.getDisMoney());
			carLevelAndPriceReVo.setSource(carCostVO.getSource());
			carLevelAndPriceReVo.setBookingStartTime(bookingStartTime);
			result.add(carLevelAndPriceReVo);
		}
		return result;
	}

	/*根據场景id查询制度集合*/
	@Override
	public List<RegimenVO> selectRegimesBySceneId(Long sceneId) {
		//根据sceneId查询制度id集合
		List<Long> regimenIds = sceneRegimeRelationMapper.selectRegimenIdsBySceneId(sceneId);
		List<RegimenVO> regimeVOs = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(regimenIds)){
			for (Long regimeId : regimenIds) {
				RegimenVO regimenVO = regimeInfoMapper.selectRegimenVOById(regimeId);
				if(regimenVO!=null){
					regimeVOs.add(regimenVO);
				}
			}
		}
		return regimeVOs;
	}

	/**
	 * 查询所有制度RegimenVO
	 * @return
	 */
	@Override
	public List<RegimenVO> selectAllRegimenVO() {
		List<RegimenVO>  all = regimeInfoMapper.selectAllRegimenVO();
		return all;
	}

	@Override
	public boolean updateRegime(RegimePo regimePo) {
		// 将旧的制度标记为已失效 
		RegimeOpt regimeOpt = new RegimeOpt();
		regimeOpt.setOptType("N111");
		regimeOpt.setRegimeId(regimePo.getRegimenId());
		regimeOpt.setOptUserId(regimePo.getOptId());
		regimeInfoMapper.updateStatus(regimeOpt);
		// 创建新的制度
		createRegime(regimePo);
		return true;
	}

	@Override
	public RegimeLimitUseCarCityInfo queryRegimeCityLimit(Long regimeId) {
		RegimeLimitUseCarCityInfo regimeLimitUseCarCityInfo = new RegimeLimitUseCarCityInfo();
		RegimeVo regimeVo = regimeInfoMapper.queryRegimeDetail(regimeId);
		if(CarConstant.USE_CAR_TYPE_OFFICIAL.equals(regimeVo.getRegimenType())){
			String ruleCity = regimeVo.getRuleCity();
			if(StringUtil.isNotEmpty(ruleCity)){
				List<String> queryLimitCityCodeList = regimeUseCarCityRuleInfoMapper.queryLimitCityCodeList(regimeId);
				if("C002".equals(ruleCity)){
					//限制可用城市
					regimeLimitUseCarCityInfo.setCanUseCityList(queryLimitCityCodeList);
				}
				if("C003".equals(ruleCity)){
					//限制不可用城市
					regimeLimitUseCarCityInfo.setNotCanUseCityList(queryLimitCityCodeList);
					
				}
			}		
		}
		return regimeLimitUseCarCityInfo;
	}

	/**
	 * 查询用户场景制度列表
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<SceneRegimensVo> getUserScenesRegimes(Long userId) throws Exception{
		//1.查询用户所有场景（排除没有关联制度的场景） 用户--制度--场景
		List<SceneInfo> sceneInfos = sceneInfoService.selectAllSceneSort(userId);
		if(CollectionUtils.isEmpty(sceneInfos)){
			return null;
		}
		//2.查询场景下的所有制度
		List<SceneRegimensVo> list = new ArrayList<>();
		SceneRegimensVo sceneRegimensVo = null;
		for (SceneInfo sceneInfo : sceneInfos) {
			sceneRegimensVo = new SceneRegimensVo();
			sceneRegimensVo.setIcon(sceneInfo.getIcon());
			sceneRegimensVo.setSceneId(sceneInfo.getSceneId());
			sceneRegimensVo.setSceneName(sceneInfo.getName());
			//根据场景id查制度集合
			List<Long> regimenIds = sceneRegimeRelationMapper.selectRegimenIdsBySceneId(sceneInfo.getSceneId());
			//根据userId查询用户的有效的regimeId集合
			List<Long> userRegimeIds = regimeInfoMapper.selectEnableRegimenIdByUserId(userId);
			regimenIds.retainAll(userRegimeIds);
			List<RegimenVO> regimenVOList = regimenIds.stream().
					map(id -> regimeInfoMapper.selectRegimenVOById(id)).filter(r -> r != null).collect(Collectors.toList());
			sceneRegimensVo.setRegimenVOS(regimenVOList);
			list.add(sceneRegimensVo);
		}
		return list;
	}

	/**
	 * 校验用车制度是否过期
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void checkRegimenExpired() {
		try {
			//查询所有制度
			List<RegimenVO> regimenVOS = regimeInfoMapper.selectAllRegimenVO();
			StringBuilder regimeIds = new StringBuilder();
			RegimeInfo regimeInfo = null;
			for (RegimenVO regimenVO : regimenVOS) {
				//过滤掉已经过期制度
				String allowDate = regimenVO.getAllowDate();
				if(allowDate != null && !ALLOW_DATA.equals(allowDate)){
					String allowEndDate = allowDate.split("-")[1];
					DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
					Date parse = dateFormat.parse(allowEndDate);
					Date date = new Date();
					if( date.getTime() > parse.getTime()){
						Long regimenId = Long.valueOf(regimenVO.getRegimenId());
						//如果制度已过期，则修改制度状态
						regimeInfo = new RegimeInfo();
						//N111已失效（无法再生效） Y000生效中 E000已停用（还能启用）
						regimeInfo.setState("N111");
						regimeInfo.setRegimenId(regimenId);
						int i = regimeInfoMapper.updateExpiredRegimeInfo(regimeInfo);
						//同时删除场景制度关系表信息
						int n = sceneRegimeRelationMapper.deleteSceneRegimeRelationByRegimeId(regimenId);
						//同时删除用户制度关系表信息
						int k = userRegimeRelationInfoMapper.deleteUserRegimeRelationInfoByRegimeId(regimenId);
						regimeIds.append(regimenVO.getRegimenId()+" ");
					}
				}
			}
			log.info(DateUtils.getMonthAndToday()+"已过期的制度id集合：{}",regimeIds);
		} catch (Exception e) {
			e.printStackTrace();
			log.info(DateUtils.getMonthAndToday()+"定时任务：checkRegimenExpired 校验过期制度异常");
		}
	}

	@Override
	public UseCarTimeVO checkUseCarTime(RegimeCheckDto regimeDto)throws Exception {
		Long regimeId = regimeDto.getRegimeId();
		RegimeVo regimeVo = regimeInfoMapper.queryRegimeDetail(regimeDto.getRegimeId());
		String allowDate = regimeVo.getAllowDate();
		if (regimeVo==null){
			throw new Exception("该制度"+regimeId+"不存在");
		}
		if (StringUtils.isNotBlank(regimeVo.getAllowDate())&&!ALLOW_DATA.equals(regimeVo.getAllowDate())){
			String[] split = regimeVo.getAllowDate().split("-");
			String time=DateFormatUtils.timeStamp2Date(regimeDto.getStartTime(),DATE_TIME_FORMAT);
			String beginDate=split[0];
			String endDate=split[1];
			String asAllowDateRound = regimeVo.getAsAllowDateRound();
			String extendBeginDate=null;
			String extendEndDate=null;
			if (StringUtils.isNotBlank(asAllowDateRound)&&!String.valueOf(CommonConstant.ZERO).equals(asAllowDateRound)){
				int day=Integer.parseInt(asAllowDateRound);
				extendBeginDate=DateFormatUtils.addDay(beginDate,-day);
				extendEndDate=DateFormatUtils.addDay(beginDate,day);
				if (DateFormatUtils.compareDate(time,extendBeginDate)==1||DateFormatUtils.compareDate(time,extendEndDate)==-1){
					throw new Exception("用车时间不在可用时间段内");
				}
				//校验差旅前后可用的时间时不校验具体的时间段只校验日期符合
				if (DateFormatUtils.compareDate(time,extendBeginDate)!=1&&DateFormatUtils.compareDate(time,split[0])==1){
					return null;
				}
				if (DateFormatUtils.compareDate(time,split[1])==-1&&DateFormatUtils.compareDate(time,extendEndDate)==1){
					return null;
				}
			}else
			if (DateFormatUtils.compareDate(time,split[0])==1||DateFormatUtils.compareDate(time,split[1])==-1){
				throw new Exception("用车时间不在可用时间段内");
			}
		}
		UseCarTimeVO useCarTimeVO=new UseCarTimeVO();
		useCarTimeVO.setRegimeId(regimeDto.getRegimeId());
		String allowDateStr=ALLOW_DATA.equals(regimeVo.getAllowDate())||StringUtils.isBlank(regimeVo.getAllowDate())?"不限":regimeVo.getAllowDate();
		useCarTimeVO.setAllowDate(allowDateStr);
		switch(regimeVo.getRuleTime()){
			case "T001":
				return null;
			case "T002":
				Map<String,Object> checkResult = checkRoleCarTimeForWoking(regimeDto.getStartTime(), regimeDto.getRegimeId());
				useCarTimeVO=useCarTimeResult(checkResult,useCarTimeVO);
				break;
			case "T003":
				Map<String,Object> checkResult1 = checkRoleCarTime(regimeDto.getStartTime(), regimeDto.getRegimeId());
				useCarTimeVO=useCarTimeResult(checkResult1,useCarTimeVO);
		}
		return useCarTimeVO;
	}


	@Override
	public List<UseCarTypeVO> checkUseCarModeAndType(RegimeCheckDto regimeDto, LoginUser loginUser) throws Exception{
		Long orgComcany=null;
		EcmpOrg ecmpOrg = orgService.getOrgByDeptId(loginUser.getUser().getDeptId());
		if (ecmpOrg!=null){
			orgComcany=ecmpOrg.getDeptId();
		}

		RegimeVo regimeVo = regimeInfoMapper.queryRegimeDetail(regimeDto.getRegimeId());
		String canUseCarMode = regimeVo.getCanUseCarMode();
		List<String> cityCodes = regimeDto.getCityCodes();
		String ruleCity = regimeVo.getRuleCity();
		if (StringUtils.isBlank(canUseCarMode)){
			log.error("制度:"+regimeDto.getRegimeId()+"未配置用车方式!");
			throw new Exception("该制度未配置用车方式!");
		}
		switch (canUseCarMode){
			case CarConstant.USR_CARD_MODE_HAVE://自由车
//				if (ApplyTypeEnum.APPLY_BUSINESS_TYPE.getKey().equals(regimeVo.getRegimenType())){
//					checkAffairsCompanyCar(orgComcany,loginUser.getUser().getDeptId(),cityCodes.get(0));
//				}else{
//				}
				String noAvailableCity = checkTraveCompanyCar(orgComcany, loginUser.getUser().getDeptId(), cityCodes);
				if (StringUtils.isNotBlank(noAvailableCity)){
					List<CityInfo> cityList=chinaCityMapper.findByCityCode(noAvailableCity.substring(1));
					String collect = cityList.stream().map(CityInfo::getCityName).collect(Collectors.joining("、", "", ""));
					throw new Exception(collect+"城市所属公司暂不支持服务");
				}
				break;
			case CarConstant.USR_CARD_MODE_NET://网约车
				
				break;
			default:

				break;
		}
		return null;
	}

	//获取开城城市
	@Override
	public List<OnLineCarTypeVO> getUseCarType(RegimeCheckDto regimeDto)throws Exception {
		RegimeVo regimeVo = regimeInfoMapper.queryRegimeDetail(regimeDto.getRegimeId());
		if (regimeVo==null){
			throw new Exception("该制度不存在");
		}
		String useCarMode = regimeDto.getUseCarMode();
		if (StringUtils.isNotBlank(useCarMode)&&CarModeEnum.ORDER_MODE_HAVE.getKey().equals(useCarMode)){

		}
		return null;
	}

	/**
	 *
	 * 校验自由车服务城市是否可用
	 * @param orgComcany
	 * @param deptId
	 * @param citys
	 * @return
	 * @throws Exception
	 */
	private String checkTraveCompanyCar(Long orgComcany,Long deptId,List<String> citys)throws Exception{
		List<CarGroupInfo> carGroupInfos = carGroupInfoMapper.selectCarGroupInfoByDeptId(orgComcany,deptId);
		if (CollectionUtils.isEmpty(carGroupInfos)){
			log.info("该公司:"+orgComcany+"下无可用/可调度车队");
			throw new Exception("当前登录人所属公司暂无企业车队");
		}
		String noAvailableCity="";
		List<Long> groupIds = carGroupInfos.stream().map(CarGroupInfo::getCarGroupId).collect(Collectors.toList());
		for (String city:citys){
			List<CarGroupServeScopeInfo> list=carGroupServeScopeInfoMapper.findByCityAndGroupId(groupIds,city);
			if (CollectionUtils.isEmpty(list)){
				noAvailableCity+=","+city;
			}
		}
		return noAvailableCity;
	}

	private String checkOnlineCar(Long orgComcany,Long deptId,List<String> citys)throws Exception{
		List<CarGroupInfo> carGroupInfos = carGroupInfoMapper.selectCarGroupInfoByDeptId(orgComcany,deptId);
		if (CollectionUtils.isEmpty(carGroupInfos)){
			log.info("该公司:"+orgComcany+"下无可用/可调度车队");
			throw new Exception("当前登录人所属公司暂无企业车队");
		}
		for(String city:citys){
			   OnLineCarTypeVO onLineCarTypeVO=new OnLineCarTypeVO();
			   if(onLineCarTypeVO!=null){
			   	
			   }
		}
		return null;
	}

	private Map<String,Object> checkRoleCarTime(String startTime,Long regimeId){
		Map<String,Object> map= Maps.newHashMap();
//		Date date = DateFormatUtils.parseDate(DateFormatUtils.DATE_FORMAT, startTime);
		Date date = new Date(Long.parseLong(startTime));
		String week = DateFormatUtils.getWeek(date);
		Integer integer = Integer.valueOf(week);
		boolean flag=false;
		List<RegimeUseCarTimeRuleInfo> regimeUseCarTimeRuleInfos = useCarTimeRuleInfoMapper.queryRegimeUseCarTimeRuleInfoList(regimeId, null);
		if (CollectionUtils.isEmpty(regimeUseCarTimeRuleInfos)){
			map.put("flag",true);
			return map;
		}
		List <RegimeUseCarTimeRuleInfo> todayList=new ArrayList<>();
		switch (integer.intValue()){
			case 1:
				todayList=regimeUseCarTimeRuleInfos.stream().filter(p->CarTimeRuleKeyEnum.CAR_TIME_KAY_D101.getKey().equals(p.getRuleKey())||CarTimeRuleKeyEnum.CAR_TIME_KAY_R101.getKey().equals(p.getRuleKey())).collect(Collectors.toList());
				break;
			case 2:
				todayList=regimeUseCarTimeRuleInfos.stream().filter(p->CarTimeRuleKeyEnum.CAR_TIME_KAY_D102.getKey().equals(p.getRuleKey())||CarTimeRuleKeyEnum.CAR_TIME_KAY_R102.getKey().equals(p.getRuleKey())).collect(Collectors.toList());
				break;
			case 3:
				todayList=regimeUseCarTimeRuleInfos.stream().filter(p->CarTimeRuleKeyEnum.CAR_TIME_KAY_D103.getKey().equals(p.getRuleKey())||CarTimeRuleKeyEnum.CAR_TIME_KAY_R103.getKey().equals(p.getRuleKey())).collect(Collectors.toList());
				break;
			case 4:
				todayList=regimeUseCarTimeRuleInfos.stream().filter(p->CarTimeRuleKeyEnum.CAR_TIME_KAY_D104.getKey().equals(p.getRuleKey())||CarTimeRuleKeyEnum.CAR_TIME_KAY_R104.getKey().equals(p.getRuleKey())).collect(Collectors.toList());
				break;
			case 5:
				todayList=regimeUseCarTimeRuleInfos.stream().filter(p->CarTimeRuleKeyEnum.CAR_TIME_KAY_D105.getKey().equals(p.getRuleKey())||CarTimeRuleKeyEnum.CAR_TIME_KAY_R105.getKey().equals(p.getRuleKey())).collect(Collectors.toList());
				break;
			case 6:
				todayList=regimeUseCarTimeRuleInfos.stream().filter(p->CarTimeRuleKeyEnum.CAR_TIME_KAY_D106.getKey().equals(p.getRuleKey())||CarTimeRuleKeyEnum.CAR_TIME_KAY_R106.getKey().equals(p.getRuleKey())).collect(Collectors.toList());
				break;
			case 0:
				todayList=regimeUseCarTimeRuleInfos.stream().filter(p->CarTimeRuleKeyEnum.CAR_TIME_KAY_D107.getKey().equals(p.getRuleKey())||CarTimeRuleKeyEnum.CAR_TIME_KAY_R107.getKey().equals(p.getRuleKey())).collect(Collectors.toList());
				break;
			default:
				break;
		}
		if (CollectionUtils.isEmpty(todayList)){
			map.put("flag",true);
			return map;
		}else{
			RegimeUseCarTimeRuleInfo regimeUseCarTimeRuleInfo = todayList.get(0);
			String key = regimeUseCarTimeRuleInfo.getRuleKey().substring(0,1);
			String time=DateFormatUtils.timeStamp2Date(startTime,TIME_FORMAT);
			if ("D".equals(key)){///次日只校验用车是否大于开始时间
				if (DateFormatUtils.compareTime(time,regimeUseCarTimeRuleInfo.getStartTime())==-1){
					map.put("flag",true);
					return map;
				}
			}else{ //校验用车时间再开始结束时间中间
				if (DateFormatUtils.compareTime(time,regimeUseCarTimeRuleInfo.getStartTime())==-1&&
						DateFormatUtils.compareTime(time,regimeUseCarTimeRuleInfo.getEndTime())==1){
					map.put("flag",true);
					return map;
				}
			}
			//校验上一天是不是结束时间为次日
			String substring = regimeUseCarTimeRuleInfo.getRuleKey().substring(1);
			if (Integer.parseInt(substring)>100){
				int lastkey= Integer.parseInt(substring) - 1;
				String newRoleKey="D"+lastkey;
				List<RegimeUseCarTimeRuleInfo> lastInfo = regimeUseCarTimeRuleInfos.stream().filter(p->newRoleKey.equals(p.getRuleKey())).collect(Collectors.toList());
				if (CollectionUtils.isNotEmpty(lastInfo)){
					RegimeUseCarTimeRuleInfo ruleInfo = lastInfo.get(0);
					if (DateFormatUtils.compareTime("00:00",time)==1&&
							DateFormatUtils.compareTime(time,ruleInfo.getEndTime())==1){
						map.put("flag",true);
						return map;
					}
				}
			}
		}
		map.put("flag",flag);
		map.put("list",regimeUseCarTimeRuleInfos);
		return map;
	}


	private Map<String,Object> checkRoleCarTimeForWoking(String startTime,Long regimeId){
		Map<String,Object> map= Maps.newHashMap();
//		Date date = DateFormatUtils.parseDate(DateFormatUtils.DATE_FORMAT, startTime);
		Date date = new Date(Long.parseLong(startTime));

		String week = DateFormatUtils.getWeek(date);
		Integer integer = Integer.valueOf(week);
		boolean flag=false;
		List<RegimeUseCarTimeRuleInfo> regimeUseCarTimeRuleInfos = useCarTimeRuleInfoMapper.queryRegimeUseCarTimeRuleInfoList(regimeId, null);
		if (CollectionUtils.isEmpty(regimeUseCarTimeRuleInfos)){
			map.put("flag",true);
			return map;
		}
		List <RegimeUseCarTimeRuleInfo> todayList=new ArrayList<>();
		if (integer.intValue()>=1&&integer.intValue()<=5){
				List<CloudWorkDateInfo> cloudWorkDateInfos = workDateInfoMapper.selectCloudWorkDateInfoList(new CloudWorkDateInfo(date));
				if (CollectionUtils.isNotEmpty(cloudWorkDateInfos)){
					if (StringUtils.isNotBlank(cloudWorkDateInfos.get(0).getFestivalName())){
						todayList=regimeUseCarTimeRuleInfos.stream().filter(p->CarTimeRuleKeyEnum.CAR_TIME_KAY_R301.getKey().equals(p.getRuleKey())||CarTimeRuleKeyEnum.CAR_TIME_KAY_R302.getKey().equals(p.getRuleKey())).collect(Collectors.toList());
					}
				}else{
					todayList=regimeUseCarTimeRuleInfos.stream().filter(p->CarTimeRuleKeyEnum.CAR_TIME_KAY_R201.getKey().equals(p.getRuleKey())||CarTimeRuleKeyEnum.CAR_TIME_KAY_R202.getKey().equals(p.getRuleKey())).collect(Collectors.toList());
				}
		}else if(integer.intValue()==6||integer.intValue()==0){
			todayList=regimeUseCarTimeRuleInfos.stream().filter(p->CarTimeRuleKeyEnum.CAR_TIME_KAY_R301.getKey().equals(p.getRuleKey())||CarTimeRuleKeyEnum.CAR_TIME_KAY_R302.getKey().equals(p.getRuleKey())).collect(Collectors.toList());
		}
		if (CollectionUtils.isEmpty(todayList)){
			map.put("flag",true);
			return map;
		}else{
			RegimeUseCarTimeRuleInfo regimeUseCarTimeRuleInfo = todayList.get(0);
			String key = regimeUseCarTimeRuleInfo.getRuleKey().substring(3);
			String time=DateFormatUtils.timeStamp2Date(startTime,TIME_FORMAT);
			if ("2".equals(key)){///次日只校验用车是否大于开始时间
				if (DateFormatUtils.compareTime(time,regimeUseCarTimeRuleInfo.getStartTime())==-1){
					map.put("flag",true);
					return map;
				}
			}else{ //校验用车时间再开始结束时间中间
				if (DateFormatUtils.compareTime(time,regimeUseCarTimeRuleInfo.getStartTime())==-1&&
						DateFormatUtils.compareTime(time,regimeUseCarTimeRuleInfo.getEndTime())==1){
					map.put("flag",true);
					return map;
				}
			}
		}
		map.put("flag",flag);
		map.put("list",regimeUseCarTimeRuleInfos);
		return map;
	}

	private UseCarTimeVO useCarTimeResult(Map<String,Object> map,UseCarTimeVO useCarTimeVO){
		boolean flag = (boolean)map.get("flag");
		if(!flag){
			List<RegimeUseCarTimeRuleInfo> regimeUseCarTimeRuleInfos=(List<RegimeUseCarTimeRuleInfo>)map.get("list");
//					List<RegimeUseCarTimeRuleInfo> regimeUseCarTimeRuleInfos = useCarTimeRuleInfoMapper.queryRegimeUseCarTimeRuleInfoList(regimeDto.getRegimeId(), null);
			if (CollectionUtils.isNotEmpty(regimeUseCarTimeRuleInfos)){
				List<CarTimeVO> carTimeVOS=new ArrayList<>();
				for (RegimeUseCarTimeRuleInfo info:regimeUseCarTimeRuleInfos){
					CarTimeVO vo=new CarTimeVO();
					BeanUtils.copyProperties(info,vo);
					vo.setStartTime("今日"+info.getStartTime());
					vo.setRuleKey(CarTimeRuleKeyEnum.format(info.getRuleKey()).getDesc());
					String substring = info.getRuleKey().substring(0, 1);
					if ("D".equals(substring)){
						vo.setEndTime(CarTimeRuleKeyEnum.format(info.getRuleKey()).getType()+info.getEndTime());
					}else{
						vo.setEndTime(CarTimeRuleKeyEnum.format(info.getRuleKey()).getType()+info.getEndTime());
					}
					carTimeVOS.add(vo);
				}
				useCarTimeVO.setUseTime(carTimeVOS);
			}

		}
		return useCarTimeVO;
	}

}
