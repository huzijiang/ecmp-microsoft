package com.hq.ecmp.mscore.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.*;
import com.hq.ecmp.mscore.bo.InvoiceAbleItineraryData;
import com.hq.ecmp.mscore.bo.JourneyBeingEndDate;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.MessageDto;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.mscore.vo.JourneyDetailVO;
import com.hq.ecmp.mscore.vo.JourneyVO;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.ecmp.util.DateFormatUtils;
import com.hq.ecmp.util.SortListUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
public class JourneyInfoServiceImpl implements IJourneyInfoService
{
    @Autowired
    private JourneyInfoMapper journeyInfoMapper;
    
    @Autowired
    private IRegimeInfoService regimeInfoService;
    
    @Autowired
    private IJourneyNodeInfoService journeyNodeInfoService;
    
    @Autowired
    private IApplyInfoService applyInfoService;
    
    @Autowired
    private IJourneyUserCarPowerService journeyUserCarPowerService;
    
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    
    @Autowired
    private ChinaCityService chinaCityService;

    @Lazy
    @Autowired
	private IOrderInfoService orderInfoService;
    @Resource
    private OrderStateTraceInfoMapper orderStateTraceInfoMapper;

    @Autowired
    private JourneyPassengerInfoMapper journeyPassengerInfoMapper;
    @Autowired
    private OrderDispatcheDetailInfoMapper dispatcheDetailInfoMapper;



    /**
     * 查询【请填写功能名称】
     *
     * @param journeyId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public JourneyInfo selectJourneyInfoById(Long journeyId)
    {
        return journeyInfoMapper.selectJourneyInfoById(journeyId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param journeyInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<JourneyInfo> selectJourneyInfoList(JourneyInfo journeyInfo)
    {
        return journeyInfoMapper.selectJourneyInfoList(journeyInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param journeyInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertJourneyInfo(JourneyInfo journeyInfo)
    {
        journeyInfo.setCreateTime(DateUtils.getNowDate());
        return journeyInfoMapper.insertJourneyInfo(journeyInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param journeyInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateJourneyInfo(JourneyInfo journeyInfo)
    {
        journeyInfo.setUpdateTime(DateUtils.getNowDate());
        return journeyInfoMapper.updateJourneyInfo(journeyInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param journeyIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteJourneyInfoByIds(Long[] journeyIds)
    {
        return journeyInfoMapper.deleteJourneyInfoByIds(journeyIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param journeyId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteJourneyInfoById(Long journeyId)
    {
        return journeyInfoMapper.deleteJourneyInfoById(journeyId);
    }

    @Override
	public List<CarAuthorityInfo> getUserCarAuthorityList(Long userId) {
        List<CarAuthorityInfo> carAuthorityInfoList = new ArrayList<>();
        //获取申请人的用车权限（已通过）
        List<JourneyInfo> journeyInfoList = journeyInfoMapper.queryPassJourneyList(userId);
        if (CollectionUtils.isEmpty(journeyInfoList)) {
            return carAuthorityInfoList;
        }
        for (JourneyInfo journeyInfo : journeyInfoList) {
            //获取是差旅还是公务
            RegimeInfo regimeInfo = regimeInfoService.queryRegimeType(journeyInfo.getRegimenId());
            if (regimeInfo == null) {
                continue;
            }
            //差旅
            if (CarConstant.USE_CAR_TYPE_TRAVEL.equals(regimeInfo.getRegimenType())) {
                if (journeyUserCarPowerService.checkJourneyNoteAllComplete(journeyInfo.getJourneyId())) {
                    //表示该行程下面所有订单都已完成了  则首页不显示这条行程
                    continue;
                }
                CarAuthorityInfo carAuthorityInfo = new CarAuthorityInfo();
                carAuthorityInfo.setJourneyId(journeyInfo.getJourneyId());
                carAuthorityInfo.setUseTime(journeyInfo.getUseTime());
                carAuthorityInfo.setType(regimeInfo.getRegimenType());
                //公务用车时间
                //TODO  .toString() 适应性添加， zc
                carAuthorityInfo.setUseDate(journeyInfo.getUseCarTime());
                //差旅类型
                Map<String, Integer> countMap = journeyUserCarPowerService.selectStatusCount(journeyInfo.getJourneyId());
                //统计差旅类型的几种用车类型的剩余次数
                carAuthorityInfo.setJoinCount(null == countMap.get(CarConstant.USE_CAR_AIRPORT_PICKUP) ? 0 : countMap.get(CarConstant.USE_CAR_AIRPORT_PICKUP));
                carAuthorityInfo.setGiveCount(null == countMap.get(CarConstant.USE_CAR_AIRPORT_DROP_OFF) ? 0 : countMap.get(CarConstant.USE_CAR_AIRPORT_DROP_OFF));
                carAuthorityInfo.setCityCount(null == countMap.get(CarConstant.CITY_USE_CAR) ? 0 : countMap.get(CarConstant.CITY_USE_CAR));
                //获取差旅的所有出差城市
                carAuthorityInfo.setCityName(journeyNodeInfoService.selectAllCity(journeyInfo.getJourneyId()));
                //获取行程的起止时间
                JourneyNodeInfo journeyNodeInfo = journeyNodeInfoService.selectMaxAndMinDate(journeyInfo.getJourneyId());
                if (null != journeyNodeInfo) {
                    carAuthorityInfo.setStartDate(journeyNodeInfo.getPlanSetoutTime());
                    carAuthorityInfo.setEndDate(journeyNodeInfo.getPlanArriveTime());
                }
                JourneyPassengerInfo passengerInfo = journeyPassengerInfoMapper.queryJourneyPassengerInfoByJourneyId(journeyInfo.getJourneyId());
                if (passengerInfo != null) {
                    carAuthorityInfo.setPassengerName(passengerInfo.getName());
                    carAuthorityInfo.setUserMobile(passengerInfo.getMobile());
                }
                Long orderId = orderInfoMapper.selectOrderIdByJourneyId(journeyInfo.getJourneyId());
                if (orderId != null) {
                    OrderDispatcheDetailInfo dispatcheDetailInfo = dispatcheDetailInfoMapper.selectDispatcheInfo(orderId);
					carAuthorityInfo.setItIsSelfDriver(dispatcheDetailInfo!=null?dispatcheDetailInfo.getItIsSelfDriver():null);
                }
                carAuthorityInfoList.add(carAuthorityInfo);
            }
            //公务
            if (CarConstant.USE_CAR_TYPE_OFFICIAL.equals(regimeInfo.getRegimenType())) {
                //查询公务用车行程下面的用车权限
                List<CarAuthorityInfo> journeyUserCarPowerList = journeyUserCarPowerService.queryJourneyAllUserAuthority(journeyInfo.getJourneyId());
                if (CollectionUtils.isEmpty(journeyUserCarPowerList)) {
                    //return carAuthorityInfoList;
					continue;
                }
                for (CarAuthorityInfo carAuthorityInfo : journeyUserCarPowerList) {
                    //根据权限Id查询对应行程节点中的起止目的地
                    JourneyNodeInfo queryJourneyNodeInfoByPowerId = journeyNodeInfoService.queryJourneyNodeInfoByPowerId(carAuthorityInfo.getTicketId());
                    String returnIsType = carAuthorityInfo.getReturnIsType();
                    carAuthorityInfo.setEndAddress(queryJourneyNodeInfoByPowerId.getPlanEndAddress());
                    carAuthorityInfo.setUseTime(journeyInfo.getUseTime());
                    //查询该权限对应的用车城市
                    String cityCode = journeyUserCarPowerService.queryOfficialPowerUseCity(carAuthorityInfo.getTicketId());
                    carAuthorityInfo.setCityCode(cityCode);
                    carAuthorityInfo.setRegimenId(journeyInfo.getRegimenId());
                    carAuthorityInfo.setJourneyId(journeyInfo.getJourneyId());
                    carAuthorityInfo.setType(regimeInfo.getRegimenType());
                    carAuthorityInfo.setServiceType(journeyInfo.getServiceType());
                    //公务用车时间
                    carAuthorityInfo.setUseDate(journeyInfo.getUseCarTime());
                    //公务类型
                    ApplyInfo applyInfo = new ApplyInfo();
                    //查询对应的公务出差理由
                    applyInfo.setJourneyId(journeyInfo.getJourneyId());
                    List<ApplyInfo> applyInfoList = applyInfoService.selectApplyInfoList(applyInfo);
                    if (null != applyInfoList && applyInfoList.size() > 0) {
                        carAuthorityInfo.setApplyName(applyInfoList.get(0).getReason());
                    }
                    //公务用车用车方式(取制度里面的)
                    carAuthorityInfo.setCarType(journeyInfo.getUseCarMode());
                    //查询改权限是否需要走调度   true-不走调度  走网约    false-走调度
                    boolean judgeNotDispatch = regimeInfoService.judgeNotDispatch(applyInfoList.get(0).getApplyId(), cityCode);
                    // 返给前端是跳转到自有车页面还是网约车页面 carType   先从订单表里取  如果没有则取是否走调度的
                    String carType;
                    Long orderId = carAuthorityInfo.getOrderId();
                    if (null != orderId) {
                        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
                        carAuthorityInfo.setCanUseCarMode(orderInfo.getUseCarMode());
                        String useCarMode = orderInfo.getUseCarMode();
                        if (StringUtil.isNotEmpty(useCarMode)) {
                            carType = useCarMode;
                        } else {
                            if (judgeNotDispatch) {
                                carType = CarConstant.USR_CARD_MODE_NET;
                            } else {
                                carType = CarConstant.USR_CARD_MODE_HAVE;
                            }
                        }
                    } else {
                        if (judgeNotDispatch) {
                            carType = CarConstant.USR_CARD_MODE_NET;
                        } else {
                            carType = CarConstant.USR_CARD_MODE_HAVE;
                        }
                    }
                    carAuthorityInfo.setCarType(carType);
                    //查询公务用车的前端状态
                    String state = journeyUserCarPowerService.buildUserAuthorityPowerStatus(judgeNotDispatch, carAuthorityInfo.getTicketId(), regimeInfo.getCompanyId());
                    carAuthorityInfo.setStatus(state);
                    if (Objects.equals(state, OrderState.ORDERDENIED.getState())) {
                        OrderStateTraceInfo orderStateTraceInfo = orderStateTraceInfoMapper.queryPowerCloseOrderIsCanle(carAuthorityInfo.getTicketId());
                        carAuthorityInfo.setRejectReason(orderStateTraceInfo.getContent());
                    }

                    JourneyPassengerInfo passengerInfo = journeyPassengerInfoMapper.queryJourneyPassengerInfoByJourneyId(journeyInfo.getJourneyId());
                    if (passengerInfo != null) {
                        carAuthorityInfo.setPassengerName(passengerInfo.getName());
                        carAuthorityInfo.setUserMobile(passengerInfo.getMobile());
                    }
                    if (orderId != null) {
                        OrderDispatcheDetailInfo dispatcheDetailInfo = dispatcheDetailInfoMapper.selectDispatcheInfo(orderId);
                        carAuthorityInfo.setItIsSelfDriver(dispatcheDetailInfo!=null?dispatcheDetailInfo.getItIsSelfDriver():null);
                    }
                    carAuthorityInfoList.add(carAuthorityInfo);
                }
            }
        }
        return carAuthorityInfoList;
    }

	@Override
public List<UserAuthorityGroupCity> getUserCarAuthority(Long journeyId) {
		List<UserAuthorityGroupCity> userAuthorityGroupCityList = new ArrayList<UserAuthorityGroupCity>();

		// 根据行程查询下面的行程节点
		JourneyNodeInfo journeyNodeQuery = new JourneyNodeInfo();
		journeyNodeQuery.setJourneyId(journeyId);
		List<JourneyNodeInfo> journeyNodeInfoList = journeyNodeInfoService.selectJourneyNodeInfoList(journeyNodeQuery);
		if (null != journeyNodeInfoList && journeyNodeInfoList.size() > 0) {
			if (journeyNodeInfoList.size() == 1) {
				//只有一个节点  北京-上海
				buildAloneNoteAuthority(journeyNodeInfoList.get(0), userAuthorityGroupCityList);
			} else {
				for (int i = 0; i < journeyNodeInfoList.size(); i++) {
					//判断当前节点的目的地是否是下一节点的出发地
					boolean flag;
					JourneyNodeInfo currentNote = journeyNodeInfoList.get(i);
					if(i==journeyNodeInfoList.size()-1){
						//当前城市的出发地是上一节点的目的地
						JourneyNodeInfo lastNote=journeyNodeInfoList.get(i-1);
						flag=lastNote.getPlanEndCityCode().equals(currentNote.getPlanBeginCityCode());
					}else{
						//当前节点的目的地是下一节点的出发地 
						JourneyNodeInfo nextNote = journeyNodeInfoList.get(i + 1);
						flag=currentNote.getPlanEndCityCode().equals(nextNote.getPlanBeginCityCode());
					}
					if (flag) {
						//当前节点的目的地是下一节点的出发地或者当前城市的出发地是上一节点的目的地  北京-上海-广州
						if(i==journeyNodeInfoList.size()-1){
							//对最后一个节点的  上海-广州 特殊处理   广州有次接机
							UserAuthorityGroupCity beginUserAuthorityGroupCity = new UserAuthorityGroupCity();
							UserAuthorityGroupCity endUserAuthorityGroupCity = new UserAuthorityGroupCity();
							List<UserCarAuthority> beginUserCarAuthorityList=new ArrayList<UserCarAuthority>();
							List<UserCarAuthority> endUserCarAuthorityList=new ArrayList<UserCarAuthority>();
							
							List<UserCarAuthority> queryNoteAllUserAuthority = journeyUserCarPowerService.queryNoteAllUserAuthority(currentNote.getNodeId(),
									currentNote.getPlanBeginCityCode());
							//取第一个type 为C001 的作为目的城市的接机权限
							for (UserCarAuthority userCarAuthority : queryNoteAllUserAuthority) {
								if(CarConstant.USE_CAR_AIRPORT_PICKUP.equals(userCarAuthority.getType()) && endUserCarAuthorityList.size()==0){
									endUserCarAuthorityList.add(userCarAuthority);
									continue;
								}
								beginUserCarAuthorityList.add(userCarAuthority);
							}
							beginUserAuthorityGroupCity.setCityName(currentNote.getPlanBeginAddress());
							beginUserAuthorityGroupCity.setVehicle(currentNote.getVehicle());
							// 开始用车城市编号
							beginUserAuthorityGroupCity.setCityId(currentNote.getPlanBeginCityCode());
							beginUserAuthorityGroupCity.setUserCarAuthorityList(beginUserCarAuthorityList);
							userAuthorityGroupCityList.add(beginUserAuthorityGroupCity);
							
							endUserAuthorityGroupCity.setCityName(currentNote.getPlanEndAddress());
							endUserAuthorityGroupCity.setVehicle(currentNote.getVehicle());
							// 结束用车城市编号
							endUserAuthorityGroupCity.setCityId(currentNote.getPlanEndCityCode());
							endUserAuthorityGroupCity.setUserCarAuthorityList(endUserCarAuthorityList);
							userAuthorityGroupCityList.add(endUserAuthorityGroupCity);
						}else{
							UserAuthorityGroupCity userAuthorityGroupCity = new UserAuthorityGroupCity();
							userAuthorityGroupCity.setCityName(currentNote.getPlanBeginAddress());
							userAuthorityGroupCity.setVehicle(currentNote.getVehicle());
							// 用车城市编号
							userAuthorityGroupCity.setCityId(currentNote.getPlanBeginCityCode());
							// 获取行程节点下的所有用户用车权限
							userAuthorityGroupCity.setUserCarAuthorityList(
									journeyUserCarPowerService.queryNoteAllUserAuthority(currentNote.getNodeId(),
											currentNote.getPlanBeginCityCode()));
							userAuthorityGroupCityList.add(userAuthorityGroupCity);
						}
						
				
						
					} else {
						// 当前节点的目的地不是下一节点的出发地 北京-上海 广州-深圳
						buildAloneNoteAuthority(currentNote, userAuthorityGroupCityList);
					}
				}
			}

		}
		/**
		 * 后面有已完成的，前面有车有司机的则还保持原来的状态
		 */
		//排序
		for (UserAuthorityGroupCity userAuthorityGroupCity:
		userAuthorityGroupCityList) {
			Collections.sort(userAuthorityGroupCity.getUserCarAuthorityList());
		}
		//查询行程的实际开始时间和结束时间
		RegimeInfo regimeInfo = regimeInfoService.queryUseCarModelByNoteId(journeyNodeInfoList.get(0).getNodeId());
		JourneyBeingEndDate validDateByJourneyNodeId = getValidDateByJourneyNodeId(journeyNodeInfoList.get(0),regimeInfo);
		//状态检查，找到返给前端的状态最后一个是待确认或者已完成的
		int innerIndex = -1;
		int outerIndex = -1;
		for (int i=0; i<userAuthorityGroupCityList.size();i++) {
			UserAuthorityGroupCity userAuthorityGroupCity = userAuthorityGroupCityList.get(i);
			userAuthorityGroupCity.setBeginDate(validDateByJourneyNodeId.getBeginDate());
			userAuthorityGroupCity.setEndDate(validDateByJourneyNodeId.getEndDate());
			List<UserCarAuthority> userCarAuthorityList = userAuthorityGroupCity.getUserCarAuthorityList();
			for (int j = 0; j < userCarAuthorityList.size(); j++) {
				UserCarAuthority userCarAuthority = userCarAuthorityList.get(j);
				if(OrderState.carAuthorityJundgeOrderCompleteFront().contains(userCarAuthority.getState())){
					innerIndex = j;
					outerIndex = i;
				}
				//市内用车，用过一次车，则前面的未使用的需要过期
				if (userCarAuthority.getType().equals(CarConstant.CITY_USE_CAR)){
					//查询权限对应的已经完成的订单
					List<Long> alreadyUsingOrderIdByPowerIds = orderInfoMapper.getAlreadyUsingOrderIdByPowerId(userCarAuthority.getTicketId());
					if(alreadyUsingOrderIdByPowerIds!=null && alreadyUsingOrderIdByPowerIds.size()>0){
						innerIndex = j;
						outerIndex = i;
					}
				}
			}
		}
		if(outerIndex != -1 && innerIndex !=-1){
			for (int i = 0; i <= outerIndex ; i++) {
				UserAuthorityGroupCity userAuthorityGroupCity = userAuthorityGroupCityList.get(i);
				List<UserCarAuthority> userCarAuthorityList = userAuthorityGroupCity.getUserCarAuthorityList();
				int index;
				if(i == outerIndex) {
					index = innerIndex-1;
				}else{
					index = userCarAuthorityList.size()-1;
				}
				for (int j = 0; j <= index; j++) {
					UserCarAuthority userCarAuthority = userCarAuthorityList.get(j);
					if(userCarAuthority.getState().equals(OrderState.INITIALIZING.getState()) ||
							userCarAuthority.getState().equals(OrderState.GETARIDE.getState())){
						userCarAuthority.setState(OrderState.TRAVELOVERUSECARTIMENOUSE.getState());
					}
					if (userCarAuthority.getState().equals(OrderState.ORDERDENIED.getState())){
						userCarAuthority.setState(OrderState.ORDERDENYNOUSE.getState());
					}
					//前端状态为待服务（已派单，准备服务，前往出发地）,则取消订单,返回前端状态变为已过期
//					if(userCarAuthority.getState().equals(OrderState.ALREADYSENDING.getState())){
//						cancelOrderAndExpiredCarAuth(userCarAuthority);
//					}
					//前端状态为派车中或者约车中，调用取消订单，返回前端状态为已过期
					if(OrderState.getWaitSendCar().contains(userCarAuthority.getState())){
						cancelOrderAndExpiredCarAuth(userCarAuthority);
					}
				}
			}
		}
		return userAuthorityGroupCityList;
	}

	/**
	 * 调用取消订单接口,并改变返给前端的状态为已过期
	 * @param userCarAuthority
	 */
	private void  cancelOrderAndExpiredCarAuth(UserCarAuthority userCarAuthority){
		OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(userCarAuthority.getOrderId());
		if(orderInfo!=null && orderInfo.getOrderId()!=null){
			try {
				orderInfoService.cancelOrder(orderInfo.getOrderId(), 1L, "用车权限过期自动取消订单");
				userCarAuthority.setState(OrderState.TRAVELOVERUSECARTIMENOUSE.getState());
			} catch (Exception e) {
				log.error("业务处理异常", e);
			}
		}
	}
	
	/**
	 * 获取单程节点  北京-上海  或者北京-上海 广州-深圳 这样行程的权限
	 * @param journeyNodeInfo
	 * @param userAuthorityGroupCityList
	 */
	private void buildAloneNoteAuthority(JourneyNodeInfo journeyNodeInfo,List<UserAuthorityGroupCity> userAuthorityGroupCityList){
		//开始城市的权限
		UserAuthorityGroupCity beginUserAuthorityGroupCity = new UserAuthorityGroupCity();
		beginUserAuthorityGroupCity.setCityName(journeyNodeInfo.getPlanBeginAddress());
		beginUserAuthorityGroupCity.setVehicle(journeyNodeInfo.getVehicle());
		//用车城市编号
		beginUserAuthorityGroupCity.setCityId(journeyNodeInfo.getPlanBeginCityCode());
		//获取行程节点下的所有用户用车权限
		List<UserCarAuthority> beginNoteAllUserAuthority = journeyUserCarPowerService.queryNoteAllUserAuthority(journeyNodeInfo.getNodeId(),journeyNodeInfo.getPlanBeginCityCode());
		log.info("行程节点编号【"+journeyNodeInfo.getNodeId()+"】单程节点出发城市查询出的权限信息:",beginNoteAllUserAuthority.toString());
		//取数组中的第一个作为开始城市的用车权限(送机)
		List<UserCarAuthority> beginUserCarAuthorityList=new ArrayList<UserCarAuthority>(1);
		beginUserCarAuthorityList.add(beginNoteAllUserAuthority.get(0));
		beginUserAuthorityGroupCity.setUserCarAuthorityList(beginUserCarAuthorityList);
		userAuthorityGroupCityList.add(beginUserAuthorityGroupCity);
		//结束城市的权限
		UserAuthorityGroupCity endUserAuthorityGroupCity = new UserAuthorityGroupCity();
		endUserAuthorityGroupCity.setCityName(journeyNodeInfo.getPlanEndAddress());
		endUserAuthorityGroupCity.setVehicle(journeyNodeInfo.getVehicle());
		endUserAuthorityGroupCity.setCityId(journeyNodeInfo.getPlanEndCityCode());//用车城市编号
		//获取行程节点下的所有用户用车权限
		List<UserCarAuthority> endNoteAllUserAuthority = journeyUserCarPowerService.queryNoteAllUserAuthority(journeyNodeInfo.getNodeId(),journeyNodeInfo.getPlanEndCityCode());
		log.info("行程节点编号【"+journeyNodeInfo.getNodeId()+"】单程节点结束城市查询出的权限信息:",endNoteAllUserAuthority.toString());
		//取数组中的最后一个作为目的城市的用车权限(接机);
		List<UserCarAuthority> endUserCarAuthorityList=new ArrayList<UserCarAuthority>(1);
		endUserCarAuthorityList.add(endNoteAllUserAuthority.get(endNoteAllUserAuthority.size()-1));
		endUserAuthorityGroupCity.setUserCarAuthorityList(endUserCarAuthorityList);
		userAuthorityGroupCityList.add(endUserAuthorityGroupCity);
	}

	@Override
	public MessageDto getJourneyMessage(Long userId) {
		return journeyInfoMapper.getJourneyMessage(userId);
	}

	/**
	 * 获取正在进行中的行程
	 * @param userId
	 * @return list
	 */
	@Override
	public List<JourneyVO> getJourneyList(Long userId){

		return journeyInfoMapper.getJourneyList(userId);
	}

	/**
	 * 获取正在进行中的行程个数
	 * @param userId
	 * @return list
	 */
	@Override
	public int getJourneyListCount(Long userId) {
		return journeyInfoMapper.getJourneyListCount(userId);
	}

	/**
	 * 根据权限id获取公务取消后的行程信息
	 * @param powerId
	 * @return
	 */
	@Override
	public JourneyDetailVO getItineraryDetail(Long powerId)throws Exception {
        JourneyDetailVO vo=new JourneyDetailVO();
		vo.setPowerId(powerId);
		JourneyUserCarPower journeyUserCarPower = journeyUserCarPowerService.selectJourneyUserCarPowerById(powerId);
//		if (journeyUserCarPower==null||CarConstant.YES_USER_USE_CAR.equals(journeyUserCarPower.getState())){
//			throw new Exception(powerId+"此用车权限以使用");
//		}
		Long applyId = journeyUserCarPower.getApplyId();
		String itIsReturn = journeyUserCarPower.getItIsReturn();
		ApplyInfo applyInfo = applyInfoService.selectApplyInfoById(applyId);
		if (applyInfo==null){
			throw new Exception(applyId+"此申请单不存在");
		}
		vo.setApplyType(applyInfo.getApplyType());
		JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(journeyUserCarPower.getJourneyId());
		vo.setServiceType(journeyInfo.getServiceType());
		vo.setCharterCarType(CharterTypeEnum.format(journeyInfo.getCharterCarType()));
		vo.setUseCarMode(journeyInfo.getUseCarMode());
		vo.setTimestamp(DateFormatUtils.formaTimestamp(journeyInfo.getUseCarTime()));
		vo.setUseCarTime(DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT_CN_3,journeyInfo.getUseCarTime()));
		if (ApplyTypeEnum.APPLY_TRAVEL_TYPE.getKey().equals(applyInfo.getApplyType())){
			return vo;
		}
        List<JourneyNodeInfo> journeyNodeInfos = journeyNodeInfoService.selectJourneyNodeInfoList(new JourneyNodeInfo(journeyUserCarPower.getJourneyId()));
        if (CollectionUtils.isNotEmpty(journeyNodeInfos)){
			vo.setStartCityCode(journeyNodeInfos.get(0).getPlanBeginCityCode());
			vo.setEndCityCode(journeyNodeInfos.get(0).getPlanEndCityCode());
            int size = journeyNodeInfos.size();
            SortListUtil.sort(journeyNodeInfos,"nodeId",SortListUtil.ASC);
            if (CommonConstant.IS_RETURN.equals(itIsReturn)){//是往返
                vo.setStartAddress(journeyNodeInfos.get(0).getPlanBeginAddress());
                vo.setStartLatitude(journeyNodeInfos.get(0).getPlanBeginLatitude());
                vo.setStartLongitude(journeyNodeInfos.get(0).getPlanBeginLongitude());
                vo.setEndLongitude(journeyNodeInfos.get(size-2).getPlanEndLongitude());
                vo.setEndLongitude(journeyNodeInfos.get(size-2).getPlanEndLongitude());
                vo.setEndLongitude(journeyNodeInfos.get(size-2).getPlanEndLongitude());
            }else{
                vo.setStartAddress(journeyNodeInfos.get(0).getPlanBeginAddress());
                vo.setStartLatitude(journeyNodeInfos.get(0).getPlanBeginLatitude());
                vo.setStartLongitude(journeyNodeInfos.get(0).getPlanBeginLongitude());
                vo.setEndAddress(journeyNodeInfos.get(size-1).getPlanEndAddress());
                vo.setEndLatitude(journeyNodeInfos.get(size-1).getPlanEndLatitude());
                vo.setEndLongitude(journeyNodeInfos.get(size-1).getPlanEndLongitude());
            }
        }
		return vo;
	}
	/**
	 * 判断是否有正在进行中的行程
	 * @param userId
	 * @return list
	 */
	@Override
	public int getWhetherJourney(Long userId) {
		return journeyInfoMapper.getWhetherJourney(userId);
	}

	/**
	 * 通过行程节点计算行程的实际可用时间
	 * @param journeyNodeInfo
	 * @return
	 */
	@Override
	public JourneyBeingEndDate getValidDateByJourneyNodeId(JourneyNodeInfo journeyNodeInfo, RegimeInfo regimeInfo){
		Long asAllowDateRound = 0L;
		if(regimeInfo != null){
			asAllowDateRound = regimeInfo.getAsAllowDateRound();
		}
		JourneyBeingEndDate journeyBeingEndDate = new JourneyBeingEndDate();
		JourneyNodeInfo journeyNodeInfo1 = new JourneyNodeInfo(journeyNodeInfo.getJourneyId());
		List<JourneyNodeInfo> journeyNodeInfoList = journeyNodeInfoService.selectJourneyNodeInfoList(journeyNodeInfo1);
		if(CollectionUtils.isNotEmpty(journeyNodeInfoList)){
			JourneyNodeInfo journeyNodeInfo3 = journeyNodeInfoList.get(0);
			JourneyNodeInfo journeyNodeInfo2 = journeyNodeInfoList.get(journeyNodeInfoList.size() - 1);
			Date BeginDate = DateUtils.addDays(journeyNodeInfo3.getPlanSetoutTime(), 0-asAllowDateRound.intValue());
			Date EndDate = DateUtils.addDays(journeyNodeInfo2.getPlanArriveTime(), asAllowDateRound.intValue());
			journeyBeingEndDate.setBeginDate(BeginDate);
			journeyBeingEndDate.setEndDate(EndDate);
		}
		return journeyBeingEndDate;
	}

	/***
	 *获取当前订单可开发票逻辑
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	public  PageResult<InvoiceAbleItineraryData>  getInvoiceAbleItinerary(Long userId,int pageNum, int pageSize) throws Exception {
		if(null!=userId){
			PageHelper.startPage(pageNum,pageSize);
			List<InvoiceAbleItineraryData> all = journeyInfoMapper.getInvoiceAbleItinerary(userId);
			PageInfo<InvoiceAbleItineraryData> pageInfo = new PageInfo<>(all);
			PageResult<InvoiceAbleItineraryData> pageResult = new PageResult<>(pageInfo.getTotal(),pageInfo.getPages(),all);
			return pageResult;
		}
		return null;
	}

	@Override
	public PageResult<InvoiceAbleItineraryData> getInvoiceAbleItineraryHistory(Long userId,int pageNum, int pageSize) throws Exception {
		if(null!=userId){
			PageHelper.startPage(pageNum,pageSize);
			List<InvoiceAbleItineraryData> all = journeyInfoMapper.getInvoiceAbleItineraryHistory(userId);
			PageInfo<InvoiceAbleItineraryData> pageInfo = new PageInfo<>(all);
			PageResult<InvoiceAbleItineraryData> pageResult = new PageResult<>(pageInfo.getTotal(),pageInfo.getPages(),all);
			return pageResult;
		}
		return null;
	}

	@Override
	public List<InvoiceAbleItineraryData> invoiceTripList(Long invoiceId) throws Exception {
		return journeyInfoMapper.invoiceTripList(invoiceId);
	}

	@Override
	public Integer getInvoiceItineraryCount(Long invoiceId) throws Exception {
		if(null!=invoiceId){
			return journeyInfoMapper.getInvoiceItineraryCount(invoiceId);
		}
		return null;
	}


}
