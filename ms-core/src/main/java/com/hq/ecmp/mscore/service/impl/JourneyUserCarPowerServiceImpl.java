package com.hq.ecmp.mscore.service.impl;

import com.github.pagehelper.util.StringUtil;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.*;
import com.hq.ecmp.mscore.bo.JourneyBeingEndDate;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.util.DateFormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
@Slf4j
public class JourneyUserCarPowerServiceImpl implements IJourneyUserCarPowerService
{
    @Autowired
    private JourneyUserCarPowerMapper journeyUserCarPowerMapper;
    @Autowired
    private IJourneyNodeInfoService journeyNodeInfoService;
    @Autowired
    private IApplyInfoService applyInfoService;
    @Autowired
    private IJourneyInfoService journeyInfoService;
    @Autowired
    private IRegimeInfoService regimeInfoService;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderStateTraceInfoMapper orderStateTraceInfoMapper;
    @Autowired
    private IEcmpConfigService ecmpConfigService;
    @Autowired
    private OrderAddressInfoMapper orderAddressInfoMapper;
    @Resource
	private ApplyUseCarTypeMapper applyUseCarTypeMapper;


    /**
     * 查询【请填写功能名称】
     *
     * @param powerId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public JourneyUserCarPower selectJourneyUserCarPowerById(Long powerId)
    {
        return journeyUserCarPowerMapper.selectJourneyUserCarPowerById(powerId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param journeyUserCarPower 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<JourneyUserCarPower> selectJourneyUserCarPowerList(JourneyUserCarPower journeyUserCarPower)
    {
        return journeyUserCarPowerMapper.selectJourneyUserCarPowerList(journeyUserCarPower);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param journeyUserCarPower 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertJourneyUserCarPower(JourneyUserCarPower journeyUserCarPower)
    {
        journeyUserCarPower.setCreateTime(DateUtils.getNowDate());
        return journeyUserCarPowerMapper.insertJourneyUserCarPower(journeyUserCarPower);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param journeyUserCarPower 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateJourneyUserCarPower(JourneyUserCarPower journeyUserCarPower)
    {
        journeyUserCarPower.setUpdateTime(DateUtils.getNowDate());
        return journeyUserCarPowerMapper.updateJourneyUserCarPower(journeyUserCarPower);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param powerIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteJourneyUserCarPowerByIds(Long[] powerIds)
    {
        return journeyUserCarPowerMapper.deleteJourneyUserCarPowerByIds(powerIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param powerId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteJourneyUserCarPowerById(Long powerId)
    {
        return journeyUserCarPowerMapper.deleteJourneyUserCarPowerById(powerId);
    }

	@Override
	public Map<String, Integer> selectStatusCount(Long journeyId) {
		//查询未使用的次数
		Map<String, Integer> map =new HashMap<>();
		List<UserAuthorityGroupCity> userCarAuthorities = journeyInfoService.getUserCarAuthority(journeyId);
		for (UserAuthorityGroupCity userAuthorityGroupCity:
		userCarAuthorities) {
			List<UserCarAuthority> userCarAuthorityList = userAuthorityGroupCity.getUserCarAuthorityList();
			for (UserCarAuthority userCarAuthority:
			userCarAuthorityList) {
				String type = userCarAuthority.getType();
				if (OrderState.noShowStateOfPower().contains(userCarAuthority.getState())){
					continue;
				}
				Integer sum = map.get(type);
				if(null ==sum){
					sum=1;
				}else{
					sum++;
				}
				map.put(type, sum);
			}
		}
		return map;
	}

	@Override
	public List<UserCarAuthority> queryNoteAllUserAuthority(Long nodeId,String cityCode) {
		List<UserCarAuthority> list = journeyUserCarPowerMapper.queryNoteAllUserAuthority(nodeId);
		 RegimeInfo regimeInfo = regimeInfoService.queryUseCarModelByNoteId(nodeId);
		//判断是否走调度   true-不走调度  走网约    false-走调度
		JourneyUserCarPower journeyUserCarPower = new JourneyUserCarPower();
		journeyUserCarPower.setNodeId(nodeId);
		List<JourneyUserCarPower> journeyUserCarPowers = journeyUserCarPowerMapper.selectJourneyUserCarPowerList(journeyUserCarPower);
		//true-不走调度  false-走调度
		 boolean flag = regimeInfoService.judgeNotDispatch(journeyUserCarPowers.get(0).getApplyId(), cityCode);
		// 查询对应的用车方式
		if (null != list && list.size() > 0) {
			RegimeInfo selectRegimeInfo = regimeInfoService.selectRegimeInfoById(regimeInfo.getRegimenId());
			for (UserCarAuthority userCarAuthority : list) {
				userCarAuthority.setRegimenId(regimeInfo.getRegimenId());
				// 返给前端是跳转到自有车页面还是网约车页面 carType   先从订单表里取  如果没有则取是否走调度的
				String carType;
				Long orderId = userCarAuthority.getOrderId();
				if (null != orderId) {
					OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
					String useCarMode = orderInfo.getUseCarMode();
					if (StringUtil.isNotEmpty(useCarMode)) {
						carType = useCarMode;
					} else {
						if (flag) {
							carType = CarConstant.USR_CARD_MODE_NET;
						} else {
							carType = CarConstant.USR_CARD_MODE_HAVE;
						}
					}
				} else {
					if (flag) {
						carType = CarConstant.USR_CARD_MODE_NET;
					} else {
						carType = CarConstant.USR_CARD_MODE_HAVE;
					}
				}
				userCarAuthority.setCarType(carType);
				// 获取接机or送机剩余次数
				userCarAuthority.handCount();
				String type = userCarAuthority.getType();
				if (CarConstant.CITY_USE_CAR.equals(type)) {
					// 如果是市内用车
					//是否允许跨城
					userCarAuthority.setSetoutEqualArrive(selectRegimeInfo.getTravelSetoutEqualArrive());
				}else{
					//接送机
					userCarAuthority.setSetoutEqualArrive(selectRegimeInfo.getAsSetoutEqualArrive());
				}
				// 生成用车权限对应的前端状态
				String state = buildUserAuthorityPowerStatus(flag, userCarAuthority.getTicketId());
				userCarAuthority.setState(state);
				if(state.equals(OrderState.ORDERDENIED.getState())){
					OrderStateTraceInfo orderStateTraceInfo = orderStateTraceInfoMapper.queryPowerCloseOrderIsCanle(userCarAuthority.getTicketId());
					userCarAuthority.setRejectReason(orderStateTraceInfo.getContent());
				}

			}
		}
		return list;
	}
	
	@Override
	public List<ServiceTypeCarAuthority> queryUserAuthorityFromService(String type, Long journeyId) {
		List<ServiceTypeCarAuthority> list = journeyUserCarPowerMapper.queryUserAuthorityFromService(type, journeyId);
		if (null != list && list.size() > 0) {
			Iterator<ServiceTypeCarAuthority> iterator = list.iterator();
			int i=0;
			while(iterator.hasNext()){
				ServiceTypeCarAuthority serviceTypeCarAuthority = iterator.next();
				i = i+1;
				if (type.equals(CarConstant.USE_CAR_AIRPORT_PICKUP)){
					if (i == 1){
						JourneyNodeInfo journeyNodeInfo = journeyNodeInfoService.selectJourneyNodeInfoById(serviceTypeCarAuthority.getNoteId());
						serviceTypeCarAuthority.setCityCode(journeyNodeInfo.getPlanEndCityCode());
						serviceTypeCarAuthority.setCityName(journeyNodeInfo.getPlanEndAddress());
					}

				}
				//剩余次数
				serviceTypeCarAuthority.parseSurplusCount();
				RegimeInfo regimeInfo = regimeInfoService.selectRegimeInfoById(serviceTypeCarAuthority.getRegimenId());
				serviceTypeCarAuthority.setCarType(regimeInfo.getCanUseCarMode());
				if (CarConstant.CITY_USE_CAR.equals(type)) {
					// 如果是市内用车
					// 是否允许跨城
					serviceTypeCarAuthority.setSetoutEqualArrive(regimeInfo.getTravelSetoutEqualArrive());
				} else {
					// 接送机
					serviceTypeCarAuthority.setSetoutEqualArrive(regimeInfo.getAsSetoutEqualArrive());
				}
				// 判断是否走调度
				JourneyUserCarPower journeyUserCarPower = new JourneyUserCarPower();
				journeyUserCarPower.setJourneyId(journeyId);
				List<JourneyUserCarPower> journeyUserCarPowers = journeyUserCarPowerMapper.selectJourneyUserCarPowerList(journeyUserCarPower);
				// true-不走调度      fasle-走调度
				boolean flag = regimeInfoService.judgeNotDispatch(journeyUserCarPowers.get(0).getApplyId(),
						serviceTypeCarAuthority.getCityCode());
				// 生成用车权限对应的前端状态
				String state = buildUserAuthorityPowerStatus(flag, serviceTypeCarAuthority.getTicketId());
				serviceTypeCarAuthority.setState(state);
				if(state.equals(OrderState.ORDERDENIED.getState())){
					OrderStateTraceInfo orderStateTraceInfo = orderStateTraceInfoMapper.queryPowerCloseOrderIsCanle(serviceTypeCarAuthority.getTicketId());
					serviceTypeCarAuthority.setRejectReason(orderStateTraceInfo.getContent());
				}
				//获取权限状态对应的有效订单
				serviceTypeCarAuthority.setOrderId(orderInfoMapper.queryVaildOrderIdByPowerId(serviceTypeCarAuthority.getTicketId()));
				//过滤掉不需要在差旅快捷入库展示的权限
				List<UserAuthorityGroupCity> userCarAuthorities = journeyInfoService.getUserCarAuthority(journeyId);
				for (UserAuthorityGroupCity userAuthorityGroupCity:
				userCarAuthorities) {
					if(userAuthorityGroupCity.getCityId().equals(serviceTypeCarAuthority.getCityCode())){
						List<UserCarAuthority> userCarAuthorityList = userAuthorityGroupCity.getUserCarAuthorityList();
						for (UserCarAuthority userCarAuthority:
						userCarAuthorityList) {
							if(userCarAuthority.getTicketId().longValue() == serviceTypeCarAuthority.getTicketId().longValue()){
								if(OrderState.noShowStateOfPower().contains(userCarAuthority.getState())){
									iterator.remove();
								}
							}
						}
					}
				}
			}
		}
		return list;
	}

	@Override
	public boolean createUseCarAuthority(Long applyId,Long auditUserId) {
		//查询行程信息
		ApplyInfo applyInfo = applyInfoService.selectApplyInfoById(applyId);
		boolean travelAllowUseCar=false;//差旅 是否允许使用城市用车
		boolean asAllowUseCar=false;//差旅  是否允许使用接送服务
		//查询行程对应的制度信息
		RegimeInfo regimeInfo = regimeInfoService.selectRegimeInfoById(applyInfo.getRegimenId());
		if(CarConstant.USE_CAR_TYPE_TRAVEL.equals(regimeInfo.getRegimenType())){
			if(CarConstant.ALLOW_USE.equals(regimeInfo.getTravelAllowInTravelCityUseCar())){
				travelAllowUseCar=true;
			}
			if(CarConstant.ALLOW_USE.equals(regimeInfo.getAsAllowAirportShuttle())){
				asAllowUseCar=true;
			}
		}
		Long journeyId = applyInfo.getJourneyId();
		//查询行程信息
		JourneyInfo journeyInfo = journeyInfoService.selectJourneyInfoById(journeyId);
		//查询行程节点信息  按节点顺序
		List<JourneyNodeInfo> journeyNodeInfoList = journeyNodeInfoService.queryJourneyNodeInfoOrderByNumber(journeyId);
		List<JourneyUserCarPower> journeyUserCarPowerList=new ArrayList<JourneyUserCarPower>();
		JourneyUserCarPower journeyUserCarPower;
		//差旅用车权限生成
		if(ApplyTypeEnum.APPLY_TRAVEL_TYPE.getKey().equals(applyInfo.getApplyType())){
			//途径地节点
			List<Long> throughTo=new ArrayList<Long>();
			//起点地
			List<Long> startTo=new ArrayList<Long>();
			//目的地
			List<Long> endTo=new ArrayList<Long>();
			if(null !=journeyNodeInfoList && journeyNodeInfoList.size()>0){
				//起点地
				startTo.add(journeyNodeInfoList.get(0).getNodeId());
				//目的地
				endTo.add(journeyNodeInfoList.get(journeyNodeInfoList.size()-1).getNodeId());
				for (int i = 1; i < journeyNodeInfoList.size(); i++) {
					JourneyNodeInfo currentNote = journeyNodeInfoList.get(i);
					JourneyNodeInfo lastNote = journeyNodeInfoList.get(i-1);
					if(null !=currentNote.getPlanBeginAddress()&& null !=lastNote.getPlanEndAddress()){
						//当前节点的出发地为上一个节点的目的地  则当前节点为途径地
						if(currentNote.getPlanBeginAddress().equals(lastNote.getPlanEndAddress())){
							throughTo.add(currentNote.getNodeId());
						}else{
							//  不是  则当前节点为新的起点地   例如  北京-上海  广州-深圳  当前广州
							startTo.add(currentNote.getNodeId());
							//上一节点还会生成一次接机权限
							endTo.add(lastNote.getNodeId());
						}
						
					}
				}
			}
			if(startTo.size()>0){
				//起点地生成一次送机权限
				for (Long s : startTo) {
					journeyUserCarPower=new JourneyUserCarPower(applyId, journeyId, new Date(), auditUserId,CarConstant.NOT_USER_USE_CAR,CarConstant.USE_CAR_AIRPORT_DROP_OFF,s);
					if(asAllowUseCar){
						journeyUserCarPowerList.add(journeyUserCarPower);
					}
					
				}
			}
			
			if(throughTo.size()>0){
				//途径地会生成市内用车   接送机权限各一次
				for (Long s : throughTo) {
					journeyUserCarPower=new JourneyUserCarPower(applyId, journeyId, new Date(), auditUserId,CarConstant.NOT_USER_USE_CAR,CarConstant.USE_CAR_AIRPORT_DROP_OFF,s);
					if(asAllowUseCar){
						journeyUserCarPowerList.add(journeyUserCarPower);
					}
					journeyUserCarPower=new JourneyUserCarPower(applyId, journeyId, new Date(), auditUserId,CarConstant.NOT_USER_USE_CAR,CarConstant.USE_CAR_AIRPORT_PICKUP,s);
					if(asAllowUseCar){
						journeyUserCarPowerList.add(journeyUserCarPower);
					}
					journeyUserCarPower=new JourneyUserCarPower(applyId, journeyId, new Date(), auditUserId,CarConstant.NOT_USER_USE_CAR,CarConstant.CITY_USE_CAR,s);
					if(travelAllowUseCar){
						journeyUserCarPowerList.add(journeyUserCarPower);
					}
					
				}
			}
			
			if(endTo.size()>0){
				//目的地生成一次接机权限
				for (Long s : endTo) {
					journeyUserCarPower=new JourneyUserCarPower(applyId, journeyId, new Date(), auditUserId,CarConstant.NOT_USER_USE_CAR,CarConstant.USE_CAR_AIRPORT_PICKUP,s);
					if(asAllowUseCar){
						journeyUserCarPowerList.add(journeyUserCarPower);
					}
					
				}
			}
		}
		
		//公务用车生成用车权限
		if(ApplyTypeEnum.APPLY_BUSINESS_TYPE.getKey().equals(applyInfo.getApplyType())){
			//判断是否是往返
			if("Y000".equals(journeyInfo.getItIsReturn())){
				//判断是否走调度  如果不走调度 则直接生成返回的用车权限 走调度则在调度选择了网约车时才生成返回的用车权限
				boolean judgeNotDispatch = regimeInfoService.judgeNotDispatch(applyId, journeyNodeInfoList.get(0).getPlanBeginCityCode());
				if(judgeNotDispatch){
					//不走调度，则只有网约车可用，网约车不许跨城
					if ("Y000".equals(regimeInfo.getSetoutEqualArrive())){
						String waitTimeStr = journeyInfo.getWaitTimeLong();
						if(StringUtil.isNotEmpty(waitTimeStr)){
							Long waitTimeLong=Long.valueOf(waitTimeStr);
							String minStr= (waitTimeLong/(1000*60))+"";
							if(ecmpConfigService.checkUpWaitMaxMinute(Long.valueOf(minStr))){
								//预估等待时长大于企业设置中的等待时长   生成返回的用车权限
								//有往返   则最后一个节点必然是返程权限
								JourneyNodeInfo backJourneyNodeInfo = journeyNodeInfoList.get(journeyNodeInfoList.size()-1);
								journeyUserCarPower=new JourneyUserCarPower(applyId, journeyId, new Date(), auditUserId,CarConstant.NOT_USER_USE_CAR,CarConstant.BACK_TRACKING,backJourneyNodeInfo.getNodeId());
								journeyUserCarPowerList.add(journeyUserCarPower);
							}
						}
					}
				}
				 //查找去程的用车权限对应的行程节点  
					//按照行程节点顺序 中   第一个出现的不是途径点的行程节点即为去程权限
					JourneyNodeInfo journeyNodeInfo = journeyNodeInfoList.get(0);
					if(null !=journeyNodeInfo.getItIsViaPoint() && "N111".equals(journeyNodeInfo.getItIsViaPoint())){
						journeyUserCarPower=new JourneyUserCarPower(applyId, journeyId, new Date(), auditUserId,CarConstant.NOT_USER_USE_CAR,CarConstant.OUTWARD_VOYAGE,journeyNodeInfo.getNodeId());
						journeyUserCarPowerList.add(journeyUserCarPower);
					}
			}else if("N444".equals(journeyInfo.getItIsReturn())){
				//没有往返
                for (int i = 0; i < journeyNodeInfoList.size(); i++) {
					//如果是途径点的行程节点则不生成用车权限
                	JourneyNodeInfo journeyNodeInfo = journeyNodeInfoList.get(i);
                	if(null!=journeyNodeInfo.getItIsViaPoint() && "Y000".equals(journeyNodeInfo.getItIsViaPoint())){
                		continue;
                	}
                	//不是途径点  则生成该行程节点去程的用车权限
                	journeyUserCarPower=new JourneyUserCarPower(applyId, journeyId, new Date(), auditUserId,CarConstant.NOT_USER_USE_CAR,CarConstant.OUTWARD_VOYAGE,journeyNodeInfo.getNodeId());
                	journeyUserCarPowerList.add(journeyUserCarPower);
                }
			}
		}
		
		//批量插入用车权限
		return journeyUserCarPowerMapper.batchInsert(journeyUserCarPowerList)>0;
	}

	@Override
	public String  buildUserAuthorityPowerStatus(boolean flag, Long powerId) {
		String vaildOrdetrState = orderInfoMapper.queryVaildOrderStatusByPowerId(powerId);
		if(StringUtil.isEmpty(vaildOrdetrState) ||OrderState.INITIALIZING.getState().equals(vaildOrdetrState)){
			//权限不可用状态  S801 -权限不可用
			JourneyUserCarPower journeyUserCarPower = journeyUserCarPowerMapper.selectJourneyUserCarPowerById(powerId);
			JourneyNodeInfo journeyNodeInfo = journeyNodeInfoService.selectJourneyNodeInfoById(journeyUserCarPower.getNodeId());
			if (journeyUserCarPower.getType().equals(CarConstant.USE_CAR_AIRPORT_PICKUP)){
				//行程的最后一个行程节点的第一个接机，则取结束城市否则取开始城市
				JourneyNodeInfo journeyNodeInfo1 = new JourneyNodeInfo();
				journeyNodeInfo1.setJourneyId(journeyNodeInfo.getJourneyId());
				List<JourneyNodeInfo> journeyNodeInfoList = journeyNodeInfoService.selectJourneyNodeInfoList(journeyNodeInfo1);
				if(CollectionUtils.isNotEmpty(journeyNodeInfoList)){
					//用车权限相关行程的最后一个行程节点
					JourneyNodeInfo journeyNodeInfo2 = journeyNodeInfoList.get(journeyNodeInfoList.size() - 1);
					if(journeyNodeInfo2.getNodeId().intValue() == journeyUserCarPower.getNodeId().intValue()){
						JourneyUserCarPower journeyUserCarPower1 = new JourneyUserCarPower();
						journeyUserCarPower1.setNodeId(journeyUserCarPower.getNodeId());
						List<JourneyUserCarPower> journeyUserCarPowers = journeyUserCarPowerMapper.selectJourneyUserCarPowerList(journeyUserCarPower1);
						for (JourneyUserCarPower journeyUserCarPower2:
						journeyUserCarPowers) {
							if(journeyUserCarPower2.getType().equals(CarConstant.USE_CAR_AIRPORT_PICKUP)){
								if(journeyUserCarPower2.getPowerId().longValue() == powerId){
								 	if(powerNotAvailable(journeyUserCarPower.getApplyId(),journeyNodeInfo.getPlanEndCityCode())){
										return OrderState.POWERNOAVAILABLE.getState();
									}
								}else{
									if(powerNotAvailable(journeyUserCarPower.getApplyId(),journeyNodeInfo.getPlanBeginCityCode())){
										return OrderState.POWERNOAVAILABLE.getState();
									}
								}
							}
						}
					}else{
						if(powerNotAvailable(journeyUserCarPower.getApplyId(),journeyNodeInfo.getPlanBeginCityCode())){
							return OrderState.POWERNOAVAILABLE.getState();
						}
					}

				}
			}else if (journeyUserCarPower.getType().equals(CarConstant.CITY_USE_CAR)){
				if (powerNotAvailable(journeyUserCarPower.getApplyId(),journeyNodeInfo.getPlanBeginCityCode())){
					return OrderState.POWERNOAVAILABLE.getState();
				}
			}else if (journeyUserCarPower.getType().equals(CarConstant.USE_CAR_AIRPORT_DROP_OFF)){
				if (powerNotAvailable(journeyUserCarPower.getApplyId(),journeyNodeInfo.getPlanBeginCityCode())){
					return OrderState.POWERNOAVAILABLE.getState();
				}
			}

			//还未生成订单  则表示权限未使用过
			if(checkPowerOverTime(powerId)){
				//已过期
				return OrderState.TIMELIMIT.getState();
			}
			if(flag){
				//对应前端状态   去约车
				return OrderState.GETARIDE.getState();
			}else{
				//对应前端状态 去申请
				return OrderState.INITIALIZING.getState();
			}
			
		}
		if(OrderState.WAITINGLIST.getState().equals(vaildOrdetrState)){
			//订单状态为待派单
			if(checkOrderOverTime(powerId)){
				//订单超时 前端状态变为去申请
				return OrderState.INITIALIZING.getState();
			}
			//未超时  对应前端状态为派车中
			return OrderState.WAITINGLIST.getState();
		}
		
		if(OrderState.SENDINGCARS.getState().equals(vaildOrdetrState)){
			//订单状态为约车中
			if(checkOrderOverTime(powerId)){
				//订单超时 前端状态变为去约车
				return OrderState.GETARIDE.getState();
			}
			  //未超时  对应前端状态为约车车中
			return OrderState.SENDINGCARS.getState();
		}
		
		if(OrderState.ALREADYSENDING.getState().equals(vaildOrdetrState) || OrderState.READYSERVICE.getState().equals(vaildOrdetrState) || OrderState.REASSIGNMENT.getState().equals(vaildOrdetrState)){
			//订单状态为已派车或者准备服务或者前往出发地   则对应前端状态为待服务
			return OrderState.ALREADYSENDING.getState();
		}
		
		if(OrderState.INSERVICE.getState().equals(vaildOrdetrState)){
			//订单状态为服务中  则对应前端状态为进行中
			return OrderState.INSERVICE.getState();
		}
		
		if(OrderState.STOPSERVICE.getState().equals(vaildOrdetrState)){
			//订单状态为服务结束  判断该订单是否需要确认
			List<String> queryUseCarMode = orderInfoMapper.queryUseCarMode(powerId);
			 int orderConfirmStatus = ecmpConfigService.getOrderConfirmStatus(ConfigTypeEnum.ORDER_CONFIRM_INFO.getConfigKey(),queryUseCarMode.get(0));
			if(orderConfirmStatus == 1){
				//需要去确认   对应前端状态为待确认-S960 
				return OrderState.WAITCONFIRMED.getState();
			}else{
				//不需要确认  对应前端状态为已完成-S699
				return OrderState.STOPSERVICE.getState();
			}
		}
		
		if(OrderState.ORDERCLOSE.getState().equals(vaildOrdetrState)){
			//订单关闭了  判断是否是取消或超时了或无车驳回
			OrderStateTraceInfo orderStateTraceInfo = orderStateTraceInfoMapper.queryPowerCloseOrderIsCanle(powerId);
			if(null !=orderStateTraceInfo && OrderStateTrace.getCancelAndOverTime().contains(orderStateTraceInfo.getState())){
				//订单是取消或超时或无车驳回的订单
				if(checkPowerOverTime(powerId)){
					//已过期
					return OrderState.TIMELIMIT.getState();
				}
				if (orderStateTraceInfo.getState().equals(OrderStateTrace.ORDEROVERTIME.getState())){
					//差旅没超过行程时间，超过用车时间，此过期状态如果后面没有使用的权限，则此权限仍可约车（定时器会去判断超过用车时间则状态变为S921）
					return OrderState.TRAVELOVERUSECARTIME.getState();
				}
				if (orderStateTraceInfo.getState().equals(OrderStateTrace.ORDERDENIED.getState())){
					return OrderState.ORDERDENIED.getState();
				}
				if(flag || queryOrderDispathIsOline(orderStateTraceInfo.getOrderId())){
					 //只有网约车  或者 调度的时候选择的是网约车   则状态改为去约车
					 return OrderState.GETARIDE.getState();
				 }else{
					 //否则就还原权限状态为去申请
					 return OrderState.INITIALIZING.getState();
				 }
			} else {
				//订单未取消 已完成
				//如果是市内用车，变为去申请或者去约车，否则变为已完成
				JourneyUserCarPower journeyUserCarPower = journeyUserCarPowerMapper.selectJourneyUserCarPowerById(powerId);
				if(journeyUserCarPower.getType().equals(CarConstant.CITY_USE_CAR)){
					if(flag){
						return OrderState.GETARIDE.getState();
					}else{
						return OrderState.INITIALIZING.getState();
					}
				}
				return OrderState.STOPSERVICE.getState();
			}
		}
		return null;
	}

	/**
	 * 判断权限是否可用,通过applyUseCarType表中这个申请单对应的这个城市是否与数据，或者有数据，数据车型都为空来进行判断
	 * @param applyId 申请单id
	 * @param cityCode 城市code
	 * @return
	 */
	private boolean powerNotAvailable(Long applyId,String cityCode) {
		ApplyUseCarType applyUseCarTypePa = new ApplyUseCarType();
		applyUseCarTypePa.setApplyId(applyId);
		applyUseCarTypePa.setCityCode(cityCode);
		List<ApplyUseCarType> applyUseCarTypes = applyUseCarTypeMapper.selectApplyUseCarTypeList(applyUseCarTypePa);
		if (CollectionUtils.isEmpty(applyUseCarTypes)){
			return true;
		}
		ApplyUseCarType applyUseCarType = applyUseCarTypes.get(0);
		if (applyUseCarType.getShuttleOwnerCarType() == null && applyUseCarType.getOwnerCarType() ==null &&
		applyUseCarType.getOnlineCarType() ==null && applyUseCarType.getShuttleOnlineCarType() == null
		){
			return true;
		}else{
			return false;
		}
	}

	private boolean queryOrderDispathIsOline(Long orderId) {
		OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
		if(null !=orderInfo && StringUtil.isNotEmpty(orderInfo.getUseCarMode()) && CarConstant.USR_CARD_MODE_NET.equals(orderInfo.getUseCarMode())){
			return true;
		}
		return false;
	}

	@Override
	public List<CarAuthorityInfo> queryJourneyAllUserAuthority(Long journeyId) {
		
		return journeyUserCarPowerMapper.queryJourneyAllUserAuthority(journeyId);
	}

	@Override
	public String queryOfficialPowerUseCity(Long powerId) {
		CarAuthorityInfo carAuthorityInfo = journeyUserCarPowerMapper.queryOfficialPowerUseCity(powerId);
		String type = carAuthorityInfo.getType();
		if(CarConstant.BACK_TRACKING.equals(type)){
			//是返程 则用车城市为目的地
			return carAuthorityInfo.getPlanEndCityCode();
		}
		//是去程  则用车城市为开始地点
		return carAuthorityInfo.getPlanBeginCityCode();
	}

	@Override
	public List<CarAuthorityInfo> queryOfficialOrderNeedPower(Long journeyId) {
		List<CarAuthorityInfo> list = journeyUserCarPowerMapper.queryOfficialOrderNeedPower(journeyId);
		if(null !=list && list.size()>0){
			for (CarAuthorityInfo carAuthorityInfo : list) {
				//查询公务权限对应的用车城市
				String cityCode = queryOfficialPowerUseCity(carAuthorityInfo.getTicketId());
				//判断该权限用车是否走调度
				boolean judgeNotDispatch = regimeInfoService.judgeNotDispatch(carAuthorityInfo.getApplyId(), cityCode);
				carAuthorityInfo.setDispatchOrder(!judgeNotDispatch);
			}
		}
		
		return list;
	}

	@Override
	public boolean updatePowerSurplus(Long powerId, Integer optType) {
		CarAuthorityInfo carAuthorityInfo = journeyUserCarPowerMapper.queryOfficialPowerUseCity(powerId);
		if(null==carAuthorityInfo || CarConstant.CITY_USE_CAR.equals(carAuthorityInfo.getType())){
			//不用更新剩余次数
			return true;
		}
		JourneyUserCarPower journeyUserCarPower = new JourneyUserCarPower();
		journeyUserCarPower.setPowerId(powerId);
		if(optType==1){
			//申请用车后  将权限标记为已使用
			journeyUserCarPower.setState(CarConstant.YES_USER_USE_CAR);
		}else{
			//取消订单后  则标记该权限为未使用
			journeyUserCarPower.setState(CarConstant.NOT_USER_USE_CAR);
		}
		journeyUserCarPower.setUpdateTime(new Date());
		return 	journeyUserCarPowerMapper.updateJourneyUserCarPower(journeyUserCarPower)>0;
	}

	
	@Override
	public boolean checkPowerOverTime(Long powerId) {
		//是否过期   当前时间是否超过了用车时间/用车时间段    公务的取开始时间   差旅的取用车时间段的结束时间
		JourneyNodeInfo journeyNodeInfo = journeyNodeInfoService.queryJourneyNodeInfoByPowerId(powerId);
		 RegimeInfo regimeInfo = regimeInfoService.queryUseCarModelByNoteId(journeyNodeInfo.getNodeId());
		 if(null ==regimeInfo){
			 return false;
		 }
		 Date planSetoutTime=null;
		 String regimenType = regimeInfo.getRegimenType();
		 if(CarConstant.USE_CAR_TYPE_OFFICIAL.equals(regimenType)){
			 //公务
			 planSetoutTime=journeyNodeInfo.getPlanSetoutTime();
		 }else if(CarConstant.USE_CAR_TYPE_TRAVEL.equals(regimenType)){
			 //差旅
			 //终止时间是最后一个行程节点的预计到达时间
			 JourneyBeingEndDate validDateByJourneyNodeId = journeyInfoService.getValidDateByJourneyNodeId(journeyNodeInfo, regimeInfo);
			 planSetoutTime=validDateByJourneyNodeId.getEndDate();
		 }
		if(null ==planSetoutTime){
			return false;
		}
		if(DateFormatUtils.beforeCurrentDate(planSetoutTime)){
			return true;
		}
		return false;
	}

	@Override
	public boolean checkOrderOverTime(Long powerId) {
		Long orderId = orderInfoMapper.queryVaildOrderIdByPowerId(powerId);
		if(null==orderId){
			return false;
		}
		OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
		String state = orderInfo.getState();
		if(OrderState.getWaitSendCar().contains(state)){
			//订单状态处于约车中或者待派单  则判断当前时间是否超过了用车时间
			OrderAddressInfo query = new OrderAddressInfo();
			query.setOrderId(orderId);
			query.setType("A000");
			OrderAddressInfo orderAddressInfo = orderAddressInfoMapper.queryOrderStartAndEndInfo(query);
			if(null==orderAddressInfo){
				return false;
			}
			//用车的开始时间
			Date startTime=orderAddressInfo.getActionTime();
			if(DateFormatUtils.beforeCurrentDate(startTime)){
				//订单已超时
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断行程是否展示 1.行程对应的每个权限都是使用或者过期则不展示
	 * 					2.或者是最后一个行程节点已经被使用则行程不展示
	 * @param journeyId
	 * @return
	 */
	@Override
	public boolean checkJourneyNoteAllComplete(Long journeyId) {
		//所有权限挨个做判断，都符合则首页行程不展示
    	JourneyUserCarPower journeyUserCarPower = new JourneyUserCarPower();
    	journeyUserCarPower.setJourneyId(journeyId);
		List<JourneyUserCarPower> journeyUserCarPowers = journeyUserCarPowerMapper.selectJourneyUserCarPowerList(journeyUserCarPower);
		if(journeyUserCarPowers.size() > 0){
			int flag = 0;
			for (JourneyUserCarPower journeyUserCarPowerCh:
			journeyUserCarPowers) {
				if(checkPowerOverTime(journeyUserCarPowerCh.getPowerId())){
						flag = flag+1;
				}else{
					String state = orderInfoMapper.queryLatestOrderByPowerId(journeyUserCarPowerCh.getPowerId());
					if(state != null){
						//判断订单是否已经完结(订单轨迹表状态为订单异议或者订单关闭或者服务完成)
						if(OrderState.carAuthorityJundgeOrderComplete().contains(state)){
							flag = flag +1;
						}
					}
				}
			}
			if(flag == journeyUserCarPowers.size()){
				return true;
			}
			//最后一个用车权限被使用则直接取消首页此行程的展示
			//查询行程对应的制度信息
			boolean asAllowUseCar =false;
			JourneyInfo journeyInfo = journeyInfoService.selectJourneyInfoById(journeyId);
			RegimeInfo regimeInfo = regimeInfoService.selectRegimeInfoById(journeyInfo.getRegimenId());
			if(CarConstant.ALLOW_USE.equals(regimeInfo.getAsAllowAirportShuttle())){
				asAllowUseCar=true;
			}
			JourneyUserCarPower lastPowerByJourneyId = null;
			if(asAllowUseCar){
				lastPowerByJourneyId = journeyUserCarPowerMapper.getLastPowerByJourneyId(journeyId);
			}else{
				lastPowerByJourneyId = journeyUserCarPowerMapper.getLastPowerCityByJourneyId(journeyId);
			}
			String state = orderInfoMapper.queryLatestOrderByPowerId(lastPowerByJourneyId.getPowerId());
			if(state != null){
				if(OrderState.carAuthorityJundgeOrderComplete().contains(state)){
					return true;
				}
			}
		}
		return false;
	}
}
