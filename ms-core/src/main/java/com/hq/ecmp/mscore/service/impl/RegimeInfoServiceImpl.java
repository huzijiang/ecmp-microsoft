package com.hq.ecmp.mscore.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.hq.api.system.domain.SysUser;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.exception.CustomException;
import com.hq.common.utils.OkHttpUtil;
import com.hq.core.security.LoginUser;
import com.hq.ecmp.constant.*;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.RegimeCheckDto;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.mscore.vo.*;
import com.hq.ecmp.util.DateFormatUtils;
import com.hq.ecmp.util.MacTools;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.Now;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.util.StringUtil;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.vo.RegimenVO;

import lombok.extern.slf4j.Slf4j;
import oshi.jna.platform.mac.SystemB;

import javax.annotation.Resource;

import static com.hq.ecmp.constant.CommonConstant.ALLOW_DATA;
import static com.hq.ecmp.util.DateFormatUtils.*;

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
    private CarInfoMapper carInfoMapper;
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
    @Resource
	private EnterpriseCarTypeInfoMapper enterpriseCarTypeInfoMapper;
    @Resource
	private ApplyUseCarTypeMapper applyUseCarTypeMapper;

	@Value("${thirdService.enterpriseId}") //企业编号
	private String enterpriseId;
	@Value("${thirdService.licenseContent}") //企业证书信息
	private String licenseContent;
	@Value("${thirdService.apiUrl}")//三方平台的接口前地址
	private String apiUrl;

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

	/**
	 * 是否不走调度
	 * @param applyId 申请单id
	 * @param cityCode 城市code
	 * @return true 不走调度  false 走调度
	 */
	@Override
	public boolean judgeNotDispatch(Long applyId, String cityCode) {
		if(StringUtil.isEmpty(cityCode)){
			return false;
		}
		if (applyId == null){
			return false;
		}
		ApplyUseCarType applyUseCarTypePa = new ApplyUseCarType();
		applyUseCarTypePa.setApplyId(applyId);
		applyUseCarTypePa.setCityCode(cityCode);
		List<ApplyUseCarType> applyUseCarTypes = applyUseCarTypeMapper.selectApplyUseCarTypeList(applyUseCarTypePa);
		if (CollectionUtils.isEmpty(applyUseCarTypes)){
			log.error("城市{}和申请单id{}确认是否调度异常",cityCode,applyId);
			return false;
		}
		ApplyUseCarType applyUseCarType = applyUseCarTypes.get(0);
		if (applyUseCarType.getShuttleOwnerCarType()!= null || applyUseCarType.getOwnerCarType() !=null ){
			return false;
		}else{
			return true;
		}
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
			//用车城市规则  C001：不限   C002：限制可用城市  C003：限制不可用城市
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
			log.error("业务处理异常", e);
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
	public String checkUseCarModeAndType(RegimeCheckDto regimeDto, LoginUser loginUser) throws Exception{
		Long orgComcany=null;
		EcmpOrg ecmpOrg = orgService.getOrgByDeptId(loginUser.getUser().getDeptId());
		if (ecmpOrg!=null){
			orgComcany=ecmpOrg.getDeptId();
		}
		String msg=null;
		String serviceType = regimeDto.getServiceType();
		RegimeVo regimeVo = regimeInfoMapper.queryRegimeDetail(regimeDto.getRegimeId());
		String canUseCarMode = regimeVo.getCanUseCarMode();
		List<String> cityCodes = Arrays.asList(regimeDto.getCityCodes().split(","));
		if (StringUtils.isBlank(canUseCarMode)){
			log.error("制度:"+regimeDto.getRegimeId()+"未配置用车方式!");
			throw new Exception("该制度未配置用车方式!");
		}
		String regimenType=regimeVo.getRegimenType();
		switch (canUseCarMode){
			case CarConstant.USR_CARD_MODE_HAVE://自由车
				List<CarGroupServeScopeInfo> noAvailableCity = checkTraveCompanyCar(orgComcany, loginUser.getUser().getDeptId(), cityCodes);
				if (CollectionUtils.isEmpty(noAvailableCity)){
//					List<CityInfo> cityList=chinaCityMapper.findByCityCode(noAvailableCity);
//					String collect = cityList.stream().map(CityInfo::getCityName).collect(Collectors.joining("、", "", ""));
					log.error(cityCodes+"城市暂无企业车队");
					throw new CustomException("该城市暂无企业车队");
				}
				break;
			case CarConstant.USR_CARD_MODE_NET://网约车
				if (CommonConstant.AFFICIAL_APPLY.equals(regimenType)) {
					if (ServiceTypeConstant.CHARTERED.equals(serviceType)){
						log.error(cityCodes+"城市所属公司网约车暂不支持服务");
						throw new CustomException("网约车不支持包车服务");
					}
				}
				List<OnLineCarTypeVO> onLineCarTypeVOS = this.threeCityServer(regimeDto.getCityCodes());
				if (CollectionUtils.isEmpty(onLineCarTypeVOS)){
					log.error(cityCodes+"城市所属公司网约车暂不支持服务");
					throw new CustomException("网约车暂未开通该城市服务");
				}else{
					if (CommonConstant.AFFICIAL_APPLY.equals(regimenType)) {
						List<CarLevelVO> carTypes = onLineCarTypeVOS.get(0).getCarTypes();
						if (CollectionUtils.isEmpty(carTypes)){
							log.error(cityCodes+"城市网约车暂不支持企业配置的车型");
							throw new CustomException("该城市暂不支持企业配置的网约车车型");
						}else{
							List<String> collect = carTypes.stream().map(CarLevelVO::getGroupId).collect(Collectors.toList());
							String onlineLevel = regimeVo.getUseCarModeOnlineLevel();
							if (StringUtils.isNotBlank(onlineLevel)){
								List<String> strings = Arrays.asList(onlineLevel.split(","));
								//与制度取交集
								collect.retainAll(strings);
								if (CollectionUtils.isEmpty(collect)){
									log.error(cityCodes+"城市网约车暂不支持企业配置的车型");
									throw new CustomException("该城市暂不支持企业配置的网约车车型");
								}
							}
						}

					}
				}
				break;
			default:
				List<CarGroupServeScopeInfo>  ownerCity = checkTraveCompanyCar(orgComcany, loginUser.getUser().getDeptId(), cityCodes);
				List<OnLineCarTypeVO> onLineCitys = this.threeCityServer(regimeDto.getCityCodes());
				if (CollectionUtils.isEmpty(ownerCity)&&CollectionUtils.isEmpty(onLineCitys)){
					throw new CustomException("该城市暂不支持自有车/网约车服务");
				}
					/*公务*/
				if (CommonConstant.AFFICIAL_APPLY.equals(regimenType)) {
					if (CollectionUtils.isEmpty(ownerCity)) {
						if (ServiceTypeConstant.CHARTERED.equals(serviceType)) {
							log.error(cityCodes + "城市所属公司网约车暂不支持包车服务");
							throw new CustomException("网约车不支持包车服务");
						} else {
							log.error(cityCodes.toString() + "城市所属公司网约车暂不支持服务");
							msg = "该城市暂无企业车队";
						}
					} else if (CollectionUtils.isEmpty(onLineCitys)) {
						log.error(cityCodes.toString() + "网约车暂未开通该城市服务");
						msg = "网约车暂未开通该城市服务";
					}

				}
				break;
		}
		return msg;
	}

	//获取开城城市
	@Override
	public List<OnLineCarTypeVO> getUseCarType(RegimeCheckDto regimeDto, SysUser user)throws Exception {
		RegimeVo regimeVo = regimeInfoMapper.queryRegimeDetail(regimeDto.getRegimeId());
		if (regimeVo==null){
			throw new Exception("该制度不存在");
		}
		List<OnLineCarTypeVO> cityCarGroup=null;
		String useCarMode = regimeDto.getUseCarMode();
		if (StringUtils.isNotBlank(useCarMode)&&CarModeEnum.ORDER_MODE_HAVE.getKey().equals(useCarMode)){
			cityCarGroup = getOwnerCityCarGroup(user.getDeptId());
		}else{
			String cityCode=StringUtils.isNotBlank(regimeDto.getCityCodes())?regimeDto.getCityCodes():"0";
			cityCarGroup=threeCityServer(cityCode);
		}
		return cityCarGroup;
	}

    /**
     * 通过申请人查询可用的制度
     * @param userId
     * @return
     */
    @Override
    public List<RegimenVO> getUserSystem(Long userId) {
        List<Long> userRegimeIds = regimeInfoMapper.selectEnableRegimenIdByUserId(userId);
        List<RegimenVO> regimenVOList = userRegimeIds.stream().
                map(id -> regimeInfoMapper.selectRegimenVOById(id)).filter(r -> r != null).collect(Collectors.toList());
        return regimenVOList;
    }

	/**
	 * 根据制度ID和城市获取具体可用车型
	 * @param regimeDto
	 * @param loginUser
	 * @return
	 */
	@Override
	public List<UseCarTypeVO> getUseCarModeAndType(RegimeCheckDto regimeDto, LoginUser loginUser) throws Exception{
		List<UseCarTypeVO> voList=new ArrayList<>();
		RegimeVo regimeVo = regimeInfoMapper.queryRegimeDetail(regimeDto.getRegimeId());
		if (regimeVo==null){
			throw new Exception("该制度不存在");
		}
		String useCarMode = regimeVo.getCanUseCarMode();
		SysUser user = loginUser.getUser();
		Long ownerCompany = loginUser.getUser().getOwnerCompany();
		String regimenType = regimeVo.getRegimenType();
		/**公务申请*/
		if (CommonConstant.AFFICIAL_APPLY.equals(regimenType)) {
			log.info("制度"+regimeDto.getRegimeId()+"-公务申请城市包括{}",regimeDto.getCityCodes());
			voList = this.getBusinessCarTypes(regimeVo, regimeDto.getServiceType(),regimeDto.getCityCodes(), useCarMode, user.getDeptId(), ownerCompany);
		}else{
			/**差旅申请*/
			log.info("制度"+regimeDto.getRegimeId()+"-差旅申请城市包括{}",regimeDto.getCityCodes());
			voList = this.getTraveCarTypes(regimeVo, regimeDto.getCityCodes(), useCarMode, user.getDeptId(), ownerCompany);

		}

		return voList;
	}

	private List<UseCarTypeVO> getCarTypeForTraveOnlie(RegimeVo regimeVo, String cityCodes)throws Exception {
		List<UseCarTypeVO> resultList=new ArrayList<>();
		String ownerCarLevel = getOnlineCarLevel(regimeVo);
		if (StringUtils.isBlank(ownerCarLevel)){
			return resultList;
		}
		List<String>  regimeCarLevel=Arrays.asList(ownerCarLevel.split(","));
		List<OnLineCarTypeVO> onLineCarTypeVOS = threeCityServer(cityCodes);
		if (CollectionUtils.isEmpty(onLineCarTypeVOS)){
			return resultList;
		}
		/**实际可用车型交集*/
		List<String> groupIds=new ArrayList<>();
		/**遍历所有城市的可用车型*/
		for (OnLineCarTypeVO vo:onLineCarTypeVOS){
			List<CarLevelVO> carTypes = vo.getCarTypes();

			if (CollectionUtils.isNotEmpty(carTypes)){
				//网约车当前城市可用车型
				List<String> collectId = carTypes.stream().map(CarLevelVO::getGroupId).collect(Collectors.toList());
				groupIds.addAll(collectId);
				UseCarTypeVO carTypeVO=new UseCarTypeVO();
				carTypeVO.setCityCode(vo.getCityId());
				List<CarServiceTypeVO> serviceTypes = vo.getServiceTypes();
				if (CollectionUtils.isNotEmpty(serviceTypes)){
					List<CarServiceTypeVO> canServiceType=serviceTypes.stream().filter(p-> OrderServiceType.ORDER_SERVICE_TYPE_PICK_UP.getBcState().equals(p.getServiceTypeId()+"")||
							OrderServiceType.ORDER_SERVICE_TYPE_SEND.getBcState().equals(p.getServiceTypeId()+"")).collect(Collectors.toList());
					/**网约车接送机可用车型*/
					if (CollectionUtils.isNotEmpty(canServiceType)){
						carTypeVO.setShuttleOnlineCarType(String.join(",",collectId));
					}
					List<CarServiceTypeVO> canType=serviceTypes.stream().filter(p-> OrderServiceType.ORDER_SERVICE_TYPE_NOW.getBcState().equals(p.getServiceTypeId()+"")||
							OrderServiceType.ORDER_SERVICE_TYPE_APPOINTMENT.getBcState().equals(p.getServiceTypeId()+"")).collect(Collectors.toList());
					/**网约车市内用车可用车型*/
					if (CollectionUtils.isNotEmpty(canType)){
						carTypeVO.setOnlineCarType(String.join(",",collectId));
					}
				}
				resultList.add(carTypeVO);
			}
		}
//		regimeCarLevel=regimeCarLevel.stream().distinct().collect(Collectors.toList());
		regimeCarLevel.retainAll(groupIds);
		List<String> result=regimeCarLevel;
		String carTypeName="";
		List<ThridCarTypeVo> onlienCarType = thirdService.getOnlienCarType();
		if (CollectionUtils.isNotEmpty(onlienCarType)){
			carTypeName=onlienCarType.stream().filter(p->result.contains(p.getName())).map(ThridCarTypeVo::getValue).collect(Collectors.joining(",","",""));
		}
		if (CollectionUtils.isNotEmpty(resultList)){
				for (UseCarTypeVO vo:resultList){
					vo.setRideHileCarType(carTypeName);
				}
			}
		return resultList;

	}

	private UseCarTypeVO getCarTypeForOnlie(RegimeVo regimeVo,String cityCodes,UseCarTypeVO vo)throws Exception{
		if (StringUtils.isBlank(regimeVo.getUseCarModeOnlineLevel())){
			return null;
		}
		List<String>  regimeCarLevel=Arrays.asList(regimeVo.getUseCarModeOnlineLevel().split(","));
		log.info("制度"+regimeVo.getRegimeId()+"配置的公务网约车等级{}",regimeCarLevel.toString());
		List<OnLineCarTypeVO> onLineCarTypeVOS = threeCityServer(cityCodes);
		if (CollectionUtils.isNotEmpty(onLineCarTypeVOS)&&CollectionUtils.isNotEmpty(onLineCarTypeVOS.get(0).getCarTypes())) {
			List<CarLevelVO> carTypes = onLineCarTypeVOS.get(0).getCarTypes();
			List<String> collectList = carTypes.stream().map(CarLevelVO::getGroupId).collect(Collectors.toList());
			//与制度取交集
			regimeCarLevel.retainAll(collectList);
			log.info(cityCodes+"城市-制度"+regimeVo.getRegimeId()+"与网约车取交集后的车型{}",regimeCarLevel.toString());
			if (CollectionUtils.isEmpty(regimeCarLevel)) {
				return null;
			} else {
				String join = String.join(",", regimeCarLevel);
				String collect = carTypes.stream().filter(p -> join.contains(p.getGroupId())).map(CarLevelVO::getGroupName).collect(Collectors.joining(",", "", ""));
				vo.setCityCode(cityCodes);
				vo.setRideHileCarType(collect);
				vo.setOnlineCarType(join);
				vo.setShuttleOnlineCarType(join);
				log.info(cityCodes+"城市网约车可用车型与制度取交集的可用车返回{}", JSONObject.toJSONString(vo));
				return vo;
			}
		}
		return null;
	}

	private UseCarTypeVO getCarTypeForOwnerBusiness(RegimeVo regimeVo,String cityCodes,UseCarTypeVO vo,Long ownerCompany,Long deptId){
		if (StringUtils.isBlank(regimeVo.getUseCarModeOwnerLevel())){
			return null;
		}
        List<Long> outerCompanyCarGroupIds = carGroupInfoMapper.queryCarGroupIdOuterCompany(cityCodes,ownerCompany);
        List<Long> innerCompanyCarGroupIds = carGroupInfoMapper.queryCarGroupIdInnerCompany(deptId, cityCodes, ownerCompany);
        if(CollectionUtils.isEmpty(outerCompanyCarGroupIds)&&CollectionUtils.isEmpty(innerCompanyCarGroupIds)){
			return null;
		}
		String regimeCarLevel=regimeVo.getUseCarModeOwnerLevel();
        log.info("制度:"+regimeVo.getRegimeId()+"配置自有车车型{}",regimeCarLevel);
		if (StringUtils.isBlank(regimeCarLevel)){
				return null;
		}

		String carTypeName=enterpriseCarTypeInfoMapper.selectCarTypesByTypeIds(ownerCompany,regimeCarLevel);
		vo.setCityCode(cityCodes);
		vo.setEnterpriseCarType(carTypeName);
		vo.setOwnerCarType(regimeCarLevel);
		vo.setShuttleOwnerCarType(regimeCarLevel);
		log.info(cityCodes+"城市的自有车可用车型名称返回类{}", JSONObject.toJSONString(vo));
		return vo;
	}

	private List<UseCarTypeVO> getCarTypeForOwnerTravel(RegimeVo regimeVo,String cityCodes,Long ownerCompany,Long deptId){
		List<UseCarTypeVO> list=new ArrayList();
		if (StringUtils.isBlank(regimeVo.getTravelUseCarModeOwnerLevel())&&StringUtils.isBlank(regimeVo.getAsUseCarModeOwnerLevel())){
			return list;
		}
		List<Long> outerCompanyCarGroupIds = carGroupInfoMapper.queryCarGroupIdOuterCompany(cityCodes,ownerCompany);
		List<Long> innerCompanyCarGroupIds = carGroupInfoMapper.queryCarGroupIdInnerCompany(deptId, cityCodes, ownerCompany);
		if(CollectionUtils.isEmpty(outerCompanyCarGroupIds)&&CollectionUtils.isEmpty(innerCompanyCarGroupIds)){
			return list;
		}
		String regimeCarLevel = this.getOwnerCarLevel(regimeVo);
		String carTypeName=enterpriseCarTypeInfoMapper.selectCarTypesByTypeIds(ownerCompany,regimeCarLevel);
		List<String> citylist=  Arrays.asList(cityCodes.split(","));
		for (String city:citylist){
			UseCarTypeVO useCarTypeVO = new UseCarTypeVO();
			useCarTypeVO.setCityCode(city);
			useCarTypeVO.setEnterpriseCarType(carTypeName);
			useCarTypeVO.setOwnerCarType(regimeCarLevel);
			useCarTypeVO.setShuttleOwnerCarType(regimeCarLevel);
			list.add(useCarTypeVO);
		}
		return list;
	}


	private String getOwnerCarLevel(RegimeVo regimeVo){
		String carLevel="";

		if (StringUtils.isNotBlank(regimeVo.getTravelUseCarModeOwnerLevel())){
			carLevel+=","+regimeVo.getTravelUseCarModeOwnerLevel();
		}
		if (StringUtils.isNotBlank(regimeVo.getAsUseCarModeOwnerLevel())){
			carLevel+=","+regimeVo.getAsUseCarModeOwnerLevel();
		}
		if (StringUtils.isNotBlank(carLevel)){
			carLevel=carLevel.substring(1);
			List<String> carLevels = Arrays.asList(carLevel.split(","));
			if (CollectionUtils.isNotEmpty(carLevels)) {
				carLevel = carLevels.stream().distinct().collect(Collectors.joining(",", "", ""));
			}
		}
		return carLevel;
	}

	private String getOnlineCarLevel(RegimeVo regimeVo){
		String carLevel="";
		if (StringUtils.isNotBlank(regimeVo.getTravelUseCarModeOnlineLevel())){
			carLevel+=","+regimeVo.getTravelUseCarModeOnlineLevel();
		}
		if (StringUtils.isNotBlank(regimeVo.getAsUseCarModeOnlineLevel())){
			carLevel+=","+regimeVo.getAsUseCarModeOnlineLevel();
		}
		if (StringUtils.isNotBlank(carLevel)){
			carLevel=carLevel.substring(1);
			List<String> carLevels = Arrays.asList(carLevel.split(","));
			if (CollectionUtils.isNotEmpty(carLevels)) {
				carLevel = carLevels.stream().distinct().collect(Collectors.joining(",", "", ""));
			}
		}
		return carLevel;
	}

	/**
	 *
	 * 校验自由车服务城市是否可用
	 * @param orgComcany
	 * @param deptId
	 * @param cityList
	 * @return
	 * @throws Exception
	 */
	private List<CarGroupServeScopeInfo> checkTraveCompanyCar(Long orgComcany,Long deptId,List<String> cityList)throws Exception{
		List<CarGroupInfo> carGroupInfos = carGroupInfoMapper.selectCarGroupInfoByDeptId(orgComcany,deptId);
		if (CollectionUtils.isEmpty(carGroupInfos)){
			log.info("该公司:"+orgComcany+"下无可用/可调度车队");
			throw new Exception("当前登录人所属公司暂无企业车队");
		}
		String noAvailableCity="";
		List<Long> groupIds = carGroupInfos.stream().map(CarGroupInfo::getCarGroupId).collect(Collectors.toList());
		String citys=String.join(",",cityList);
		List<CarGroupServeScopeInfo> list=carGroupServeScopeInfoMapper.findByCityAndGroupId(groupIds,citys);
		log.info(cityList.toString()+"城市支持的车队{}",list.toString());
		return list;
	}

	private List<OnLineCarTypeVO> checkOnlineCar(List<String> citys)throws Exception{
		String join = String.join(",", citys);
		List<OnLineCarTypeVO> onLineCarTypeVOS = this.threeCityServer(join);
		return onLineCarTypeVOS;
	}

	//获取登录人所属公司自由车开城情况
	private List<OnLineCarTypeVO> getOwnerCityCarGroup(Long deptId) throws Exception{
		EcmpOrg ecmpOrg = orgService.getOrgByDeptId(deptId);
		List<CarGroupInfo> carGroupInfos = carGroupInfoMapper.selectCarGroupInfoList(new CarGroupInfo(ecmpOrg.getCompanyId()));
		if (CollectionUtils.isEmpty(carGroupInfos)){
			throw new Exception("该申请人所属公司暂无车队服务");
		}
		List<Long> collect = carGroupInfos.stream().map(CarGroupInfo::getCarGroupId).collect(Collectors.toList());
		List<OnLineCarTypeVO> cityList=carInfoMapper.findByGroupIds(collect);
		if(CollectionUtils.isNotEmpty(cityList)){
			for (OnLineCarTypeVO vo:cityList){
				List<CarLevelVO> carType =carGroupInfoMapper.findCarTypeByGroupIds(vo.getCarGroupIds());
				vo.setCarTypes(carType);
			}
		}
		return cityList;
	}

	private Map<String,Object> checkRoleCarTime(String startTime,Long regimeId){
		Map<String,Object> map= Maps.newHashMap();
//		Date date = DateFormatUtils.parseDate(DateFormatUtils.DATE_FORMAT, startTime);
		Date date = new Date(Long.parseLong(startTime));
		String week = DateFormatUtils.getWeek(date);
		Integer weekint = Integer.valueOf(week);
		boolean flag=false;
		List<RegimeUseCarTimeRuleInfo> regimeUseCarTimeRuleInfos = useCarTimeRuleInfoMapper.queryRegimeUseCarTimeRuleInfoList(regimeId, null);
		if (CollectionUtils.isEmpty(regimeUseCarTimeRuleInfos)){
			map.put("flag",true);
			return map;
		}
		List <RegimeUseCarTimeRuleInfo> todayList=new ArrayList<>();
		todayList=checkWeek(weekint,regimeUseCarTimeRuleInfos,todayList);
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


	private List<RegimeUseCarTimeRuleInfo> checkWeek(Integer weekint,List<RegimeUseCarTimeRuleInfo> regimeUseCarTimeRuleInfos,List <RegimeUseCarTimeRuleInfo> todayList){
		switch (weekint.intValue()){
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
		return todayList;
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

	private List<OnLineCarTypeVO> threeCityServer(String cityCodes)throws Exception{
//		String join = String.join(",", cityCodes);
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("enterpriseId", enterpriseId);
		paramMap.put("cityCodes", cityCodes);
		paramMap.put("licenseContent", licenseContent);
		paramMap.put("mac",  MacTools.getMacList().get(0));
		log.info("网约车开城及车型{}参数{}",cityCodes,paramMap);
		String result = OkHttpUtil.postForm(apiUrl + "/basic/cityService", paramMap);
		log.info("网约车开城及车型{}返回结果{}",cityCodes,result);
		JSONObject jsonObject = JSONObject.parseObject(result);
		if (ApiResponse.SUCCESS_CODE!=jsonObject.getInteger("code")) {
			throw new Exception("调用三方获取网约车开城情况-》失败!");
		}
		String data = jsonObject.getString("data");
		List<OnLineCarTypeVO> list = JSONObject.parseArray(data, OnLineCarTypeVO.class);
		if (CollectionUtils.isNotEmpty(list)){
			list=list.stream().filter(p->CollectionUtils.isNotEmpty(p.getCarTypes())).collect(Collectors.toList());
		}
		return list;
	}

	/**差旅申请可用车型*/
	private List<UseCarTypeVO> getTraveCarTypes(RegimeVo regimeVo,String cityCodes,String useCarMode,Long deptId,Long ownerCompany)throws Exception{
		List<UseCarTypeVO> voList=new ArrayList<>();
		if (CarModeEnum.ORDER_MODE_HAVE.getKey().equals(useCarMode)){
			voList = getCarTypeForOwnerTravel(regimeVo, cityCodes,ownerCompany,deptId);
		}else if (CarModeEnum.ORDER_MODE_NET.getKey().equals(useCarMode)){
			/*网约车*/
			List<UseCarTypeVO> carTypeList = getCarTypeForTraveOnlie(regimeVo, cityCodes);
			if (CollectionUtils.isNotEmpty(carTypeList)){
				voList.addAll(carTypeList);
			}
		}else{
			/**自有车+网约车*/
			/*自有车*/
			List<UseCarTypeVO> ownerCarTypes = getCarTypeForOwnerTravel(regimeVo, cityCodes, ownerCompany,deptId);
			List<UseCarTypeVO> onlineCarType = getCarTypeForTraveOnlie(regimeVo, cityCodes);
			if (CollectionUtils.isNotEmpty(ownerCarTypes)&&CollectionUtils.isNotEmpty(onlineCarType)){
				if (ownerCarTypes.size()>=onlineCarType.size()){
					for (UseCarTypeVO vo:ownerCarTypes){
						List<UseCarTypeVO> collect = onlineCarType.stream().filter(p -> vo.getCityCode().equals(p.getCityCode())).collect(Collectors.toList());
						if (CollectionUtils.isNotEmpty(collect)){
							vo.setRideHileCarType(collect.get(0).getRideHileCarType());
						}
					}
					voList.addAll(ownerCarTypes);
				}else {
					for (UseCarTypeVO vo : onlineCarType) {
						List<UseCarTypeVO> collect = ownerCarTypes.stream().filter(p -> vo.getCityCode().equals(p.getCityCode())).collect(Collectors.toList());
						if (CollectionUtils.isNotEmpty(collect)) {
							vo.setEnterpriseCarType(collect.get(0).getEnterpriseCarType());
						}
					}
					voList.addAll(onlineCarType);
				}
			}else if (CollectionUtils.isNotEmpty(ownerCarTypes)&&CollectionUtils.isEmpty(onlineCarType)){
				/**差旅城市只有自有车*/
				voList.addAll(ownerCarTypes);
			}else if (CollectionUtils.isEmpty(ownerCarTypes)&&CollectionUtils.isNotEmpty(onlineCarType)){
				/**差旅城市只有网约车*/
				voList.addAll(onlineCarType);
			}
		}
		return voList;
	}

	/**公务申请可用车型*/
	private List<UseCarTypeVO> getBusinessCarTypes(RegimeVo regimeVo,String serviceType,String cityCodes,String useCarMode,Long deptId,Long ownerCompany)throws Exception{
		List<UseCarTypeVO> voList=new ArrayList<>();
		if (CarModeEnum.ORDER_MODE_HAVE.getKey().equals(useCarMode)){
			/**不考虑往返*/
			UseCarTypeVO vo = getCarTypeForOwnerBusiness(regimeVo, cityCodes, new UseCarTypeVO(), ownerCompany,deptId);
			if (vo!=null){
				voList.add(vo);
			}
		}else if (CarModeEnum.ORDER_MODE_NET.getKey().equals(useCarMode)){
			if (!ServiceTypeConstant.CHARTERED.equals(serviceType)){
				/*网约车*/
				UseCarTypeVO carTypefor = getCarTypeForOnlie(regimeVo, cityCodes, new UseCarTypeVO());
				if (carTypefor != null) {
					voList.add(carTypefor);
				}
			}
		}else{
			/**自有车+网约车*/
			/*自有车*/
			UseCarTypeVO vo = getCarTypeForOwnerBusiness(regimeVo, cityCodes, new UseCarTypeVO(), ownerCompany,deptId);
			UseCarTypeVO carTypefor=null;
			if (!ServiceTypeConstant.CHARTERED.equals(serviceType)) {
				carTypefor = getCarTypeForOnlie(regimeVo, cityCodes, new UseCarTypeVO());
			}
			if (vo!=null&&carTypefor!=null){
				vo.setRideHileCarType(carTypefor.getRideHileCarType());
				vo.setOnlineCarType(carTypefor.getOnlineCarType());
				vo.setShuttleOnlineCarType(carTypefor.getShuttleOnlineCarType());
				voList.add(vo);
			}else if(vo==null&&carTypefor!=null){
				voList.add(carTypefor);
			}else if (vo!=null&&carTypefor==null){
				voList.add(vo);
			}
		}
		return voList;
	}

}
